package org.example.omnibecard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class CardBenefitResDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetCardBenefit{

        private Long cardBenefitId;
        private Long benefitId;
        private String title;
        private String sponsorName;
        private Float discountRate;
        private LocalDate updatedAt;
        private LocalDate endDate;
        private String status;
        private String targetProduct;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetCardBenefitPage{

        List<CardBenefitResDto.GetCardBenefit> cardBenefits;
        boolean isFirst;
        boolean isLast;
        int pageSize;
        long totalElements;

    }


}
