package org.example.omnibecard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.omnibecard.common.apiPayload.ApiResult;
import org.example.omnibecard.dto.CardBenefitReqDto;
import org.example.omnibecard.dto.CardBenefitResDto;
import org.example.omnibecard.entity.CardBenefit;
import org.example.omnibecard.service.CardBenefitService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card/v1")
public class CardBenefitController {

    private final CardBenefitService cardBenefitService;

    public CardBenefitController(CardBenefitService cardBenefitService) {
        this.cardBenefitService = cardBenefitService;
    }

    @PostMapping("/cardBenefits")
    @Operation(summary = "카드 혜택 생성 Api",description = " 서비스 끼리 통신입니다. ",tags = "Service-CardBenefit")
    public ApiResult<Void> createCardBenefit(@RequestBody CardBenefitReqDto.CreateCardBenefit createCardBenefitDto){
        cardBenefitService.createCardBenefit(createCardBenefitDto);
        return ApiResult.onSuccess();

    }

    @GetMapping("/my/cardBenefits")
    @Operation(summary = "전체 카드 혜택 Api",description = " 페이징 필요 - ( page, size 만 적어도 됨, 스웨거에서 sort 는 배열이 아닌 빈문자열로 넣어주세요 )  - ( 엑세스 토큰 필요 )",tags = "CardBenefit")
    public ApiResult<CardBenefitResDto.GetCardBenefitPage> getCardBenefits(@PageableDefault(size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                           @Parameter(hidden = true) @RequestHeader("X-Authorization-Id") Long memberId){

        return ApiResult.onSuccess(cardBenefitService.getCardBenefits(memberId, pageable));

    }

    @GetMapping("/my/cardBenefits/available")
    @Operation(summary = "사용가능한 카드 혜택 Api",description = " 상태가 ONGOING인 카드혜택만 보여집니다. - ( 엑세스 토큰 필요 )",tags = "CardBenefit")
    public ApiResult<List<CardBenefitResDto.GetCardBenefit>> getAvailableCardBenefits(@Parameter(hidden = true) @RequestHeader("X-Authorization-Id") Long memberId){

        return ApiResult.onSuccess(cardBenefitService.getAvailableCardBenefit(memberId));

    }

    @PostMapping("/cardBenefits/check")
    @Operation(summary = "카드 혜택 확인하기 Api",description = " 카드 번호 16자리를 입력해주세요. - ( 엑세스 토큰 필요 )",tags = "CardBenefit")
    public ApiResult<List<CardBenefitResDto.GetCardBenefit>> checkAvailableCardBenefits(@RequestBody CardBenefitReqDto.CheckAvailableCardBenefit checkAvailableCardBenefit){

        return ApiResult.onSuccess(cardBenefitService.checkAvailableCardBenefit(checkAvailableCardBenefit));

    }

    @PostMapping("/cardBenefits/sync-status")
    @Operation(summary = "카드 혜택 변경 Api",description = " 서비스 끼리 통신입니다. ",tags = "Service-CardBenefit")
    public ApiResult<?> syncCardBenefit(@RequestBody List<CardBenefitReqDto.SyncCardBenefit> syncCardBenefitList){
        cardBenefitService.syncCardBenefit(syncCardBenefitList);
        return ApiResult.onSuccess();

    }
}
