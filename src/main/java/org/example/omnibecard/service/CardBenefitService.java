package org.example.omnibecard.service;

import org.example.omnibecard.dto.CardBenefitReqDto;
import org.example.omnibecard.dto.CardBenefitResDto;
import org.example.omnibecard.entity.Card;
import org.example.omnibecard.entity.CardBenefit;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CardBenefitService {

    List<Long> createCardBenefit(CardBenefitReqDto.CreateCardBenefit cardBenefitDto);
    CardBenefitResDto.GetCardBenefitPage getCardBenefits(Long memberId, Pageable pageable);
    List<CardBenefitResDto.GetCardBenefit> getAvailableCardBenefit(Long memberId);
    List<CardBenefitResDto.GetCardBenefit> checkAvailableCardBenefit(CardBenefitReqDto.CheckAvailableCardBenefit dto);
    void syncCardBenefit(List<CardBenefitReqDto.SyncCardBenefit> syncCardBenefitList);
    boolean existsCardBenefit(Long benefitId);
    Map<Long, CardBenefit> getLatestBenefits(List<Card> cards);
}
