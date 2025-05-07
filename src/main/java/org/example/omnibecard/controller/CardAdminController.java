package org.example.omnibecard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.omnibecard.common.apiPayload.ApiResult;
import org.example.omnibecard.common.apiPayload.code.status.ErrorStatus;
import org.example.omnibecard.common.apiPayload.exception.GeneralException;
import org.example.omnibecard.dto.CardResDto;
import org.example.omnibecard.entity.Card;
import org.example.omnibecard.service.CardAdminService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card/v1/admin")
public class CardAdminController {

    private final CardAdminService cardAdminService;

    public CardAdminController(CardAdminService cardAdminService) {
        this.cardAdminService = cardAdminService;
    }

    @GetMapping("/members")
    @Operation(summary = "전체 사용자 카드 정보 가져오기 Api",description = " 관리자 전용입니다. - ( 엑세스 토큰 필요 + 관리자 로그인 필요 )",tags = "Admin-Card")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "402", description = "COMMON402-금지된 요청입니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<CardResDto.GetCardForAdminPage> getCardForAdmin(@Parameter(hidden = true) @RequestHeader("X-Authorization-Role") String role,
                                                                     @PageableDefault(size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable){

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }

        return ApiResult.onSuccess(cardAdminService.getCardForAdmin(pageable));
    }

}
