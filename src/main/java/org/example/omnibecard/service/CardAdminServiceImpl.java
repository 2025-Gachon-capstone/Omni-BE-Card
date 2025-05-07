package org.example.omnibecard.service;

import lombok.extern.slf4j.Slf4j;
import org.example.omnibecard.client.SponsorClient;
import org.example.omnibecard.client.UserClient;
import org.example.omnibecard.common.apiPayload.ApiResult;
import org.example.omnibecard.common.apiPayload.code.status.ErrorStatus;
import org.example.omnibecard.common.apiPayload.exception.GeneralException;
import org.example.omnibecard.converter.CardConverter;
import org.example.omnibecard.dto.BenefitResDto;
import org.example.omnibecard.dto.CardResDto;
import org.example.omnibecard.dto.MemberResDto;
import org.example.omnibecard.entity.Card;
import org.example.omnibecard.entity.CardBenefit;
import org.example.omnibecard.repository.CardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CardAdminServiceImpl implements CardAdminService {

    private final CardRepository cardRepository;
    private final CardBenefitService cardBenefitService;
    private final UserClient userClient;
    private final SponsorClient sponsorClient;

    public CardAdminServiceImpl(CardRepository cardRepository, CardBenefitService cardBenefitService,
                                UserClient userClient, SponsorClient sponsorClient) {
        this.cardRepository = cardRepository;
        this.cardBenefitService = cardBenefitService;
        this.userClient = userClient;
        this.sponsorClient = sponsorClient;
    }

    @Override
    public CardResDto.GetCardForAdminPage getCardForAdmin(Pageable pageable) {
        Page<Card> cards = cardRepository.findAll(pageable);

        Set<Long> memberIds = cards.stream()
                .map(Card::getMemberId)
                .collect(Collectors.toSet());

        ApiResult<List<MemberResDto.GetMemberList>> memberRes;

        try {
            memberRes = userClient.getMemberList(new ArrayList<>(memberIds));
        } catch (Exception e) {
            log.error("Feign 통신 오류: 유저 서버 호출 실패", e);
            throw new GeneralException(ErrorStatus._USER_SERVICE_ERROR);
        }

        Map<Long, MemberResDto.GetMemberList> memberMap = memberRes.getResult().stream()
                .collect(Collectors.toMap(MemberResDto.GetMemberList::getMemberId, Function.identity()));

        Map<Long, CardBenefit> latestBenefitMap = cardBenefitService.getLatestBenefits(cards.getContent());

        Set<Long> benefitIds = latestBenefitMap.values().stream()
                .map(CardBenefit::getBenefitId)
                .collect(Collectors.toSet());

        ApiResult<List<BenefitResDto.GetBatchBenefit>> benefitRes;

        try {
            benefitRes = sponsorClient.getBatchBenefits(new ArrayList<>(benefitIds));
        } catch (Exception e) {
            log.error("Feign 통신 오류: 스폰서 서버 호출 실패", e);
            throw new GeneralException(ErrorStatus._SPONSOR_SERVICE_ERROR);
        }

        Map<Long, BenefitResDto.GetBatchBenefit> benefitMap = benefitRes.getResult().stream()
                .collect(Collectors.toMap(BenefitResDto.GetBatchBenefit::getBenefitId, Function.identity()));

        return CardConverter.toGetCardForAdminPage(cards, memberMap, latestBenefitMap, benefitMap);
    }

}
