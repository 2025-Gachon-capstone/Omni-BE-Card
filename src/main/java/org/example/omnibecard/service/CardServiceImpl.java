package org.example.omnibecard.service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.example.omnibecard.client.UserClient;
import org.example.omnibecard.common.apiPayload.ApiResult;
import org.example.omnibecard.common.apiPayload.code.status.ErrorStatus;
import org.example.omnibecard.common.apiPayload.exception.GeneralException;
import org.example.omnibecard.common.util.CardGenerator;
import org.example.omnibecard.dto.CardReqDto;
import org.example.omnibecard.dto.UserResDto;
import org.example.omnibecard.entity.Card;
import org.example.omnibecard.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class CardServiceImpl implements CardService {

    @Autowired
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserClient userClient;

    public CardServiceImpl(CardRepository cardRepository, PasswordEncoder passwordEncoder,
                           UserClient userClient) {
        this.cardRepository = cardRepository;
        this.passwordEncoder = passwordEncoder;
        this.userClient = userClient;
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
    public void verifyCard(String loginId, String cardPassword) {

        Long memberId;

        try {
            ApiResult<UserResDto.GetMemberId> response = userClient.getMemberId(loginId);

            if (!response.getIsSuccess()) {
                throw new GeneralException(ErrorStatus._NOT_FOUND_LOGINID);
            }

            memberId = response.getResult().getMemberId();

        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._USER_SERVICE_ERROR); // 유저 서버 연결 실패
        }

        Card card = cardRepository.findByMemberId(memberId)
                .orElseThrow(()->new GeneralException(ErrorStatus._NOT_FOUND_CARD));

        if(!passwordEncoder.matches(cardPassword, card.getCardPassword())){
            throw new GeneralException(ErrorStatus._NOT_MATCH_CARDPASSWORD);
        }
    }
}
