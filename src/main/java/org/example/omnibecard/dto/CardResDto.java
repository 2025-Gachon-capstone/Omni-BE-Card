package org.example.omnibecard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CardResDto {


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetCard{

        private Long cardId;
        private String cardNumber;
        private String memberName;
        private String securityCode;
        private LocalDateTime createdAt;
        private LocalDateTime expired;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetCardSummary{

        private Long cardId;
        private String cardNumber;
        private LocalDateTime createdAt;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetCardSummaryPage{

        List<CardResDto.GetCardSummary> cards;
        boolean isFirst;
        boolean isLast;
        int pageSize;
        long totalElements;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetMemberId{

        private Long memberId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetCardForAdmin{

        private Long memberId;
        private String loginId;
        private String memberName;
        private String cardNumber;
        private String createdAt;
        private String updatedAt;
        private String benefitTitle;
        private String status;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetCardForAdminPage{

        List<CardResDto.GetCardForAdmin> cards;
        boolean isFirst;
        boolean isLast;
        int pageSize;
        long totalElements;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetCardDetailForAdmin{

        private Long memberId;
        private Long cardId;
        private String memberName;
        private String cardNumber;
        private String createdAt;
        private String updatedAt;
        private List<BenefitResDto.GetBenefitTitle> cardBenefits;

    }

}
