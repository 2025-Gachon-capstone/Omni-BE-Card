package org.example.omnibecard.service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.example.omnibecard.client.SponsorClient;
import org.example.omnibecard.common.apiPayload.ApiResult;
import org.example.omnibecard.common.apiPayload.code.status.ErrorStatus;
import org.example.omnibecard.common.apiPayload.exception.GeneralException;
import org.example.omnibecard.controller.CardBenefitController;
import org.example.omnibecard.converter.CardBenefitConverter;
import org.example.omnibecard.dto.BenefitResDto;
import org.example.omnibecard.dto.CardBenefitReqDto;
import org.example.omnibecard.dto.CardBenefitResDto;
import org.example.omnibecard.entity.Card;
import org.example.omnibecard.entity.CardBenefit;
import org.example.omnibecard.entity.type.CardBenefitStatus;
import org.example.omnibecard.repository.CardBenefitRepository;
import org.example.omnibecard.repository.CardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CardBenefitServiceImpl implements CardBenefitService {

    private final SponsorClient sponsorClient;
    private final CardBenefitRepository cardBenefitRepository;
    private final CardRepository cardRepository;

    public CardBenefitServiceImpl(SponsorClient sponsorClient, CardBenefitRepository cardBenefitRepository,
                                  CardRepository cardRepository) {
        this.sponsorClient = sponsorClient;
        this.cardBenefitRepository = cardBenefitRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    @Transactional
    public void createCardBenefit(CardBenefitReqDto.CreateCardBenefit cardBenefitDto) {

        // 1. 스폰서 서비스에서 혜택 정보 가져오기
        BenefitResDto.GetBenefit benefitDto;
        try {
            ApiResult<BenefitResDto.GetBenefit> apiResult = sponsorClient.getBenefit(cardBenefitDto.getBenefitId());
            benefitDto = apiResult.getResult();

            if (benefitDto == null) {
                log.warn("benefitDto가 null입니다!");
                throw new GeneralException(ErrorStatus._INVALID_BENEFIT_STATUS);
            }

            log.info("benefitDto: id={}, amount={}, status={}",
                    benefitDto.getBenefitId(), benefitDto.getAmount(), benefitDto.getBenefitStatus());

        } catch (FeignException e) {
            log.error("스폰서 서비스 호출 중 오류: {}", e.contentUTF8());
            throw new GeneralException(ErrorStatus._SPONSOR_SERVICE_ERROR);
        }

        // 2. 혜택 상태 검증
        String statusStr = benefitDto.getBenefitStatus();
        if ("DELETED".equals(statusStr) || "EXPIRED".equals(statusStr)) {
            throw new GeneralException(ErrorStatus._INVALID_BENEFIT_STATUS);
        }

        // 3. 혜택 수량 검증
        if (benefitDto.getAmount() < cardBenefitDto.getMemberIdList().size()) {
            throw new GeneralException(ErrorStatus._EXCEED_BENEFIT_AMOUNT);
        }

        // 4. 전체 카드혜택 조회 (모든 상태 포함)
        List<CardBenefit> allCardBenefits = cardBenefitRepository.findAllByBenefitId(benefitDto.getBenefitId());

        // 5. memberId → CardBenefit 맵핑
        Map<Long, CardBenefit> memberIdToCardBenefitMap = allCardBenefits.stream()
                .collect(Collectors.toMap(cb -> cb.getCard().getMemberId(), cb -> cb, (cb1, cb2) -> cb1));

        // 6. 기존 유효 상태 (ONGOING, BEFORE 등)인 멤버 추출
        Set<Long> existingValidMemberIds = allCardBenefits.stream()
                .filter(cb -> cb.getStatus() != CardBenefitStatus.DELETED &&
                        cb.getStatus() != CardBenefitStatus.EXPIRED)
                .map(cb -> cb.getCard().getMemberId())
                .collect(Collectors.toSet());

        Set<Long> requestedMemberIds = new HashSet<>(cardBenefitDto.getMemberIdList());

        // 7. 삭제 대상: 기존 유효 멤버 중 요청에서 빠진 멤버
        Set<Long> toDelete = new HashSet<>(existingValidMemberIds);
        toDelete.removeAll(requestedMemberIds);

        for (Long memberId : toDelete) {
            CardBenefit cardBenefit = memberIdToCardBenefitMap.get(memberId);
            if (cardBenefit != null && cardBenefit.getStatus() != CardBenefitStatus.DELETED &&
                    cardBenefit.getStatus() != CardBenefitStatus.EXPIRED) {
                cardBenefit.setStatus(CardBenefitStatus.DELETED);
            }
        }

        // 8. 추가 대상: 요청에만 포함된 멤버 (기존 유효 멤버가 아님)
        Set<Long> toAdd = new HashSet<>(requestedMemberIds);
        toAdd.removeAll(existingValidMemberIds);

        CardBenefitStatus newStatus = CardBenefitStatus.valueOf(statusStr);

        for (Long memberId : toAdd) {
            CardBenefit existingDeleted = memberIdToCardBenefitMap.get(memberId);

            if (existingDeleted != null && existingDeleted.getStatus() == CardBenefitStatus.DELETED) {
                // 과거 삭제된 혜택 → 상태 복원
                existingDeleted.setStatus(newStatus);
            } else {
                // 완전히 새로운 혜택 → 생성
                Card card = cardRepository.findByMemberId(memberId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_CARD));

                CardBenefit newCardBenefit = CardBenefit.builder()
                        .card(card)
                        .benefitId(benefitDto.getBenefitId())
                        .status(newStatus)
                        .build();

                card.getCardBenefits().add(newCardBenefit);
                cardBenefitRepository.save(newCardBenefit);
            }
        }
    }

    @Override
    public CardBenefitResDto.GetCardBenefitPage getCardBenefits(Long memberId, Pageable pageable) {

        Card card = cardRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_CARD));

        Page<CardBenefit> cardBenefits = cardBenefitRepository.findByCard_CardId(card.getCardId(), pageable);

        List<Long> benefitIds = cardBenefits.stream()
                .map(CardBenefit::getBenefitId)
                .distinct()
                .toList();

        List<BenefitResDto.GetBatchBenefit> getBatchBenefits;

        try {
            ApiResult<List<BenefitResDto.GetBatchBenefit>> response = sponsorClient.getBatchBenefits(benefitIds);
            getBatchBenefits = response.getResult();
        } catch (FeignException e) {
            log.error("스폰서 호출 실패: {}", e.contentUTF8());
            throw new GeneralException(ErrorStatus._SPONSOR_SERVICE_ERROR);
        }

        Map<Long, BenefitResDto.GetBatchBenefit> benefitMap = getBatchBenefits.stream()
                .collect(Collectors.toMap(BenefitResDto.GetBatchBenefit::getBenefitId, b -> b));

        return CardBenefitConverter.toGetCardBenefitPage(cardBenefits, benefitMap);

    }

    @Override
    @Transactional
    public void syncCardBenefit(List<CardBenefitReqDto.SyncCardBenefit> syncCardBenefitList) {

        for (CardBenefitReqDto.SyncCardBenefit dto : syncCardBenefitList) {
            CardBenefitStatus newStatus = CardBenefitStatus.valueOf(dto.getNewStatus());

            List<CardBenefit> benefitsToUpdate = cardBenefitRepository.findByBenefitIdAndStatusIn(
                    dto.getBenefitId(),
                    List.of(CardBenefitStatus.BEFORE, CardBenefitStatus.ONGOING)
            );

            for (CardBenefit cb : benefitsToUpdate) {
                cb.setStatus(newStatus);
                cardBenefitRepository.save(cb);
            }
        }
    }

}
