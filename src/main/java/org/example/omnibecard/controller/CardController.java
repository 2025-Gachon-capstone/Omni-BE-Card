package org.example.omnibecard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.omnibecard.common.apiPayload.ApiResult;
import org.example.omnibecard.common.apiPayload.code.status.ErrorStatus;
import org.example.omnibecard.common.apiPayload.exception.GeneralException;
import org.example.omnibecard.converter.CardConverter;
import org.example.omnibecard.dto.CardReqDto;
import org.example.omnibecard.dto.CardResDto;
import org.example.omnibecard.entity.Card;
import org.example.omnibecard.service.CardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card/v1")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/cards")
    @Operation(summary = "카드 생성 Api",description = "서비스 통신 전용 입니다. ",tags = "Service-Card")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4001-1", description = "CARD4001-이미 존재하는 사용자입니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4001-2", description = "DATABASE4001-데이터베이스 저장 오류입니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "5001", description = "CARD5001-중복된 카드 번호로 인한 생성 실패입니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<Void> createCard(@RequestBody CardReqDto.CreateCard request) {

        cardService.createCard(request);
        return ApiResult.onSuccess();

    }

    @PostMapping("/cards/self")
    @Operation(summary = "카드 생성 Api",description = "카드 생성 API 입니다.",tags = "Card")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4008", description = "MEMBER4008-사용자를 찾지 못하였습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),

    })
    public ApiResult<Void> createCardByUser(@Parameter(hidden = true) @RequestHeader("X-Authorization-Id") Long memberId,
                                            @Valid @RequestBody CardReqDto.CreateCardByUser request) {

        cardService.createCardByUser(request,memberId);
        return ApiResult.onSuccess();

    }

    @PostMapping("/cards/verify")
    @Operation(summary = "카드 비밀번호 인증 Api",description = "카드 비밀번호 4자리 입력해주세요. ( 엑세스 토큰 필요 ) ",tags = "Card")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4002", description = "CARD4002-사용자의 카드가 없습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4003", description = "CARD4003-카드비밀번호가 일치하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<?> verifyCardPassword(@Parameter(hidden = true) @RequestHeader("X-Authorization-Id") Long memberId,
                                           @Valid @RequestBody CardReqDto.VerifyCardPassword verifyCardPasswordDto) {
        cardService.verifyCard(memberId, verifyCardPasswordDto.getCardPassword());
        return ApiResult.onSuccess();
    }

    @GetMapping("/my/cards")
    @Operation(summary = "카드 정보 불러오기 Api",description = " 안전을 위해 확인된 비밀번호를 다시 보내주세요. ( 엑세스 토큰 필요 ) ",tags = "Card")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4002", description = "CARD4002-사용자의 카드가 없습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<CardResDto.GetCardSummaryPage> getCard(@Parameter(hidden = true) @RequestHeader("X-Authorization-Id") Long memberId,
                                                 @RequestParam(defaultValue = "0") int page){

        return ApiResult.onSuccess(cardService.getCard(memberId,page));
    }

    @PostMapping("/memberId")
    @Operation(summary = "memberId 가져오기 Api",description = " 서비스끼리 통신입니다. ",tags = "Service-Card")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4002", description = "CARD4002-사용자의 카드가 없습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<CardResDto.GetMemberId> getCard(@RequestBody CardReqDto.GetMemberId getMemberIdDto){

        return ApiResult.onSuccess(cardService.getMemberId(getMemberIdDto));
    }

}
