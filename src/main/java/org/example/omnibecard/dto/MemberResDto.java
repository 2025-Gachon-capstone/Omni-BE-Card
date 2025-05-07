package org.example.omnibecard.dto;

import lombok.Getter;

public class MemberResDto {

    @Getter
    public static class GetMemberList{

        private Long memberId;
        private String memberName;
        private String loginId;
        private String status;

    }

}
