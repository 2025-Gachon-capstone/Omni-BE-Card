package org.example.omnibecard.client;

import org.example.omnibecard.common.apiPayload.ApiResult;
import org.example.omnibecard.dto.CardReqDto;
import org.example.omnibecard.dto.UserResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "userClient", url = "${USER_SERVER_ADDRESS}")
public interface UserClient {

    // memberId 가져오기
    @GetMapping("/user/v1/auth/members/{loginId}")
    ApiResult<UserResDto.GetMemberId> getMemberId(@PathVariable("loginId") String loginId);

}

