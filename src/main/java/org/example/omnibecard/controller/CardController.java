package org.example.omnibecard.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.omnibecard.common.apiPayload.ApiResult;
import org.example.omnibecard.dto.CardReqDto;
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
    @Operation(summary = "카드 생성 Api",description = "회원가입시 카드 생성하는 API입니다.",tags = "Card")
    public ApiResult<Void> createCard(@RequestBody CardReqDto.CreateCard request) {

        cardService.createCard(request);
        return ApiResult.onSuccess();

    }

}
