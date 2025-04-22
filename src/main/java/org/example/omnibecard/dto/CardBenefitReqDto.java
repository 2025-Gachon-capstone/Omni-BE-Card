package org.example.omnibecard.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class CardBenefitReqDto {

    @Getter
    public static class CreateCardBenefit{

        @NotNull(message = "benefitId는 필수입니다.")
        private Long benefitId;

        @NotNull(message = "memberIdList는 필수입니다.")
        private List<Long> memberIdList;

    }

    @Getter
    public static class SyncCardBenefit{

        private Long benefitId;
        private String newStatus;

    }

}
