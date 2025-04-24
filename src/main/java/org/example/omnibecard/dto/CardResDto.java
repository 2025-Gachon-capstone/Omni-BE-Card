package org.example.omnibecard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    public static class GetMemberId{

        private Long memberId;
    }


}
