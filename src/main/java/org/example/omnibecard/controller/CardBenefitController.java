package org.example.omnibecard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.omnibecard.common.apiPayload.ApiResult;
import org.example.omnibecard.dto.CardBenefitReqDto;
import org.example.omnibecard.service.CardBenefitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card/v1/cardBenefits")
public class CardBenefitController {

    private final CardBenefitService cardBenefitService;

    public CardBenefitController(CardBenefitService cardBenefitService) {
        this.cardBenefitService = cardBenefitService;
    }

    @PostMapping
    @Operation(summary = "카드 혜택 생성 Api",description = " 서비스 끼리 통신입니다. ",tags = "Service-CardBenefit")
    public ApiResult<Void> createCardBenefit(@RequestBody CardBenefitReqDto.CreateCardBenefit createCardBenefitDto){
        cardBenefitService.createCardBenefit(createCardBenefitDto);
        return ApiResult.onSuccess();

    }

    @PostMapping("/sync-status")
    @Operation(summary = "카드 혜택 변경 Api",description = " 서비스 끼리 통신입니다. ",tags = "Service-CardBenefit")
    public ApiResult<?> syncCardBenefit(@RequestBody List<CardBenefitReqDto.SyncCardBenefit> syncCardBenefitList){
        cardBenefitService.syncCardBenefit(syncCardBenefitList);
        return ApiResult.onSuccess();

    }
}
