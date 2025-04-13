package org.example.omnibecard.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.omnibecard.common.apiPayload.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card/v1")
public class CardController {

    @GetMapping("/test")
    @Operation(summary = "테스트 API",description = "테스트를 위한 api입니다.",tags = "Card")
    public ApiResult<?> test() {

        return ApiResult.onSuccess("test is successful");

    }

}
