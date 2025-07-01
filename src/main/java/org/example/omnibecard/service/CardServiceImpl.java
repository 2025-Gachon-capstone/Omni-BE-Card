package org.example.omnibecard.service;

import lombok.extern.slf4j.Slf4j;
import org.example.omnibecard.client.SponsorClient;
import org.example.omnibecard.client.UserClient;
import org.example.omnibecard.common.apiPayload.code.status.ErrorStatus;
import org.example.omnibecard.common.apiPayload.exception.GeneralException;
import org.example.omnibecard.common.util.CardGenerator;
import org.example.omnibecard.converter.CardConverter;
import org.example.omnibecard.dto.CardReqDto;
import org.example.omnibecard.dto.CardResDto;
import org.example.omnibecard.entity.Card;
import org.example.omnibecard.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CardServiceImpl implements CardService {

    @Autowired
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserClient userClient;
    private final CardBenefitService cardBenefitService;
    private final SponsorClient sponsorClient;

    public CardServiceImpl(CardRepository cardRepository,PasswordEncoder passwordEncoder,
                           UserClient userClient, CardBenefitService cardBenefitService,
                           SponsorClient sponsorClient) {
        this.cardRepository = cardRepository;
        this.userClient = userClient;
        this.cardBenefitService = cardBenefitService;
        this.sponsorClient = sponsorClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void createCard(CardReqDto.CreateCard createCardDto) {

        if (cardRepository.existsByMemberId(createCardDto.getMemberId())) {
            throw new GeneralException(ErrorStatus._CARD_ALREADY_EXISTS); // 이건 새로 정의해도 좋아
        }

        String cardNumber;
        int maxTry = 10;
        int attempt = 0;

        while(attempt++ < maxTry){
            cardNumber = CardGenerator.generateCardNumber();

            if (!cardRepository.existsByCardNumber(cardNumber)) {
                Card card = Card.builder()
                        .memberId(createCardDto.getMemberId())
                        .memberName(createCardDto.getMemberName())
                        .cardNumber(cardNumber)
                        .cardPassword(passwordEncoder.encode(createCardDto.getCardPassword()))
                        .securityCode(CardGenerator.generateSecurityCode())
                        .expiredDate(LocalDateTime.now().plusYears(5))
                        .build();

                try {
                    Card savedCard = cardRepository.save(card);
                    log.info("Saved Card: {}", savedCard.getCardNumber());
                    return;
                } catch (DataAccessException e) {
                    throw new GeneralException(ErrorStatus._DATABASE_SAVE_ERROR); // 새로 정의해도 됨
                }
            }

        }
        throw new GeneralException(ErrorStatus._CARD_NUMBER_GENERATION_FAILED);

    }

    @Override
    public void createCardByUser(CardReqDto.CreateCardByUser createCardDto, Long memberId) {

        List<Card> existingCards = cardRepository.findAllByMemberId(memberId);

        if (existingCards.isEmpty()) {
            throw new GeneralException(ErrorStatus._NOT_FOUND_MEMBER);
        }

        String memberName = existingCards.get(0).getMemberName();

        String cardNumber;
        int maxTry = 10;
        int attempt = 0;

        while(attempt++ < maxTry){
            cardNumber = CardGenerator.generateCardNumber();

            if (!cardRepository.existsByCardNumber(cardNumber)) {
                Card card = Card.builder()
                        .memberId(memberId)
                        .memberName(memberName)
                        .cardNumber(cardNumber)
                        .cardPassword(passwordEncoder.encode(createCardDto.getCardPassword()))
                        .securityCode(CardGenerator.generateSecurityCode())
                        .expiredDate(LocalDateTime.now().plusYears(5))
                        .build();

                try {
                    Card savedCard = cardRepository.save(card);
                    log.info("Saved Card: {}", savedCard.getCardNumber());
                    return;
                } catch (DataAccessException e) {
                    throw new GeneralException(ErrorStatus._DATABASE_SAVE_ERROR); // 새로 정의해도 됨
                }
            }

        }
        throw new GeneralException(ErrorStatus._CARD_NUMBER_GENERATION_FAILED);

    }

    @Override
    public void verifyCard(Long memberId, String cardPassword) {

        Card card = cardRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_CARD));

        if (!passwordEncoder.matches(cardPassword, card.getCardPassword())) {
            throw new GeneralException(ErrorStatus._NOT_MATCH_CARDPASSWORD);
        }

    }

    @Override
    public CardResDto.GetCardSummaryPage getCard(Long memberId, int page) {

        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Card> cardPage = cardRepository.findAllByMemberId(memberId, pageable);

        if (cardPage.isEmpty()) {
            throw new GeneralException(ErrorStatus._NOT_FOUND_CARD);
        }

        return CardConverter.toGetCardSummaryPage(cardPage);
    }

    @Override
    public CardResDto.GetMemberId getMemberId(CardReqDto.GetMemberId getMemberIdDto) {

        Card card = cardRepository.findByCardNumber(getMemberIdDto.getCardNumber())
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_CARD));

        return new CardResDto.GetMemberId(card.getMemberId());
    }

}
