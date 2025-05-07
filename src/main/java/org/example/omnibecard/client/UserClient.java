package org.example.omnibecard.client;

import org.example.omnibecard.common.apiPayload.ApiResult;
import org.example.omnibecard.dto.MemberResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "userClient", url = "${USER_SERVER_ADDRESS}")
public interface UserClient {

    @PostMapping("/user/v1/auth/memberList")
    ApiResult<List<MemberResDto.GetMemberList>> getMemberList(@RequestBody List<Long>memberIds);

}

