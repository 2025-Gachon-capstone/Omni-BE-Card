package org.example.omnibecard.client;

import org.example.omnibecard.common.apiPayload.ApiResult;
import org.example.omnibecard.dto.BenefitResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "sponsorClient", url = "${SPONSOR_SERVER_ADDRESS}")
public interface SponsorClient {

    // 회원가입시 카드 생성 요청
    @GetMapping("/sponsor/v1/benefits/{benefitId}")
    ApiResult<BenefitResDto.GetBenefit> getBenefit(@PathVariable("benefitId") Long benefitId);

}
