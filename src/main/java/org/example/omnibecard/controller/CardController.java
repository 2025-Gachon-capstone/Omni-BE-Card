package org.example.omnibecard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.example.omnibecard.common.apiPayload.ApiResult;
import org.example.omnibecard.converter.CardConverter;
import org.example.omnibecard.dto.CardReqDto;
import org.example.omnibecard.dto.CardResDto;
import org.example.omnibecard.entity.Card;
import org.example.omnibecard.service.CardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card/v1")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/cards")
    @Operation(summary = "카드 생성 Api",description = "서비스 통신 전용 입니다. ",tags = "Service")
    public ApiResult<Void> createCard(@RequestBody CardReqDto.CreateCard request) {

        cardService.createCard(request);
        return ApiResult.onSuccess();

    }

    @PostMapping("/cards/verify")
    @Operation(summary = "카드 비밀번호 인증 Api",description = "카드 비밀번호 4자리 입력해주세요. ",tags = "Card")
    public ApiResult<?> verifyCardPassword(@Parameter(hidden = true) @RequestHeader("X-Authorization-Id") String loginId,
                                           @Valid @RequestBody CardReqDto.VerifyCardPassword verifyCardPasswordDto) {
        cardService.verifyCard(loginId, verifyCardPasswordDto.getCardPassword());
        return ApiResult.onSuccess();
    }

    @GetMapping("/my/cards")
    @Operation(summary = "카드 정보 불러오기 Api",description = "카드 비밀번호는 서버에서 줄수 없어요...",tags = "Card")
    public ApiResult<CardResDto.GetCard> getCard(@Parameter(hidden = true) @RequestHeader("X-Authorization-Id") String loginId){

        Card card = cardService.getCard(loginId);
        return ApiResult.onSuccess(CardConverter.toGetCard(card));
    }

}
