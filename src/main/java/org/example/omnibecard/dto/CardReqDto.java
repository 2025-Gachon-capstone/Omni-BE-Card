package org.example.omnibecard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class CardReqDto {

    @Getter
    public static class CreateCard {

        private Long memberId;
        private String memberName;
        private String cardPassword;

    }

    @Getter
    public static class VerifyCardPassword {

        @NotBlank(message = "카드 비밀번호는 필수입니다.")
        private String cardPassword;

    }

    @Getter
    public static class GetMemberId{

        private String cardNumber;

    }

}
