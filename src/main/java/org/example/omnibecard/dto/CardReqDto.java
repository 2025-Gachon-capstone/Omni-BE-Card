package org.example.omnibecard.dto;

import lombok.Getter;

public class CardReqDto {

    @Getter
    public static class CreateCard {

        private Long memberId;
        private String memberName;
        private String cardPassword;

    }

}
