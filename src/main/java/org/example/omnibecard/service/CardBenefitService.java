package org.example.omnibecard.service;

import org.example.omnibecard.dto.CardBenefitReqDto;

import java.util.List;

public interface CardBenefitService {

    void createCardBenefit(CardBenefitReqDto.CreateCardBenefit cardBenefitDto);
    void syncCardBenefit(List<CardBenefitReqDto.SyncCardBenefit> syncCardBenefitList);
}
