package org.example.omnibecard.converter;

import org.example.omnibecard.dto.BenefitResDto;
import org.example.omnibecard.dto.CardBenefitResDto;
import org.example.omnibecard.entity.CardBenefit;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public class CardBenefitConverter {

    public static CardBenefitResDto.GetCardBenefit toGetCardBenefit(CardBenefit cb, BenefitResDto.GetBatchBenefit benefit) {
        return CardBenefitResDto.GetCardBenefit.builder()
                .cardBenefitId(cb.getCardBenefitId())
                .benefitId(cb.getBenefitId())
                .title(benefit.getTitle())
                .sponsorName(benefit.getSponsorName())
                .discountRate(benefit.getDiscountRate())
                .updatedAt(cb.getUpdatedAt().toLocalDate())
                .endDate(benefit.getEndDate())
                .status(String.valueOf(cb.getStatus()))
                .build();
    }

    public static CardBenefitResDto.GetCardBenefitPage toGetCardBenefitPage(
            Page<CardBenefit> cardBenefits,
            Map<Long, BenefitResDto.GetBatchBenefit> benefitMap
    ) {
        List<CardBenefitResDto.GetCardBenefit> converted = cardBenefits.stream()
                .filter(cb -> benefitMap.containsKey(cb.getBenefitId()))
                .map(cb -> toGetCardBenefit(cb, benefitMap.get(cb.getBenefitId())))
                .toList();

        return CardBenefitResDto.GetCardBenefitPage.builder()
                .cardBenefits(converted)
                .isFirst(cardBenefits.isFirst())
                .isLast(cardBenefits.isLast())
                .pageSize(cardBenefits.getTotalPages())
                .totalElements(cardBenefits.getTotalElements())
                .build();
    }
}
