package org.example.omnibecard.client;

import org.example.omnibecard.common.apiPayload.ApiResult;
import org.example.omnibecard.dto.BenefitResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(name = "sponsorClient", url = "${SPONSOR_SERVER_ADDRESS}")
public interface SponsorClient {

    // 혜택 가져오기
    @GetMapping("/sponsor/v1/benefits/{benefitId}")
    ApiResult<BenefitResDto.GetBenefit> getBenefit(@PathVariable("benefitId") Long benefitId);

    // 리스트로 헤택 가져오기
    @PostMapping("/sponsor/v1/benefits/batch")
    ApiResult<List<BenefitResDto.GetBatchBenefit>> getBatchBenefits(@RequestBody List<Long> benefitIds);

}
