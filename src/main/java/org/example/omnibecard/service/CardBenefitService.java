package org.example.omnibecard.service;

import org.example.omnibecard.dto.CardBenefitReqDto;
import org.example.omnibecard.dto.CardBenefitResDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CardBenefitService {

    void createCardBenefit(CardBenefitReqDto.CreateCardBenefit cardBenefitDto);
    CardBenefitResDto.GetCardBenefitPage getCardBenefits(Long memberId, Pageable pageable);
    List<CardBenefitResDto.GetCardBenefit> getAvailableCardBenefit(Long memberId);
    List<CardBenefitResDto.GetCardBenefit> checkAvailableCardBenefit(CardBenefitReqDto.CheckAvailableCardBenefit dto);
    void syncCardBenefit(List<CardBenefitReqDto.SyncCardBenefit> syncCardBenefitList);
    boolean existsCardBenefit(Long benefitId);
}
