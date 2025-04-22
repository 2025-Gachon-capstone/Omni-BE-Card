package org.example.omnibecard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class BenefitResDto {

    @Getter
    public static class GetBenefit{

        private Long benefitId;
        private int amount;
        private String benefitStatus;

    }

    @Getter
    public static class GetBatchBenefit{

        private Long benefitId;
        private String title;
        private String sponsorName;
        private Float discountRate;
        private LocalDate endDate;
        private int amount;

    }

}
