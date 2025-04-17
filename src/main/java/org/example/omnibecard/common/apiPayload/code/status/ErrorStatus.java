package org.example.omnibecard.common.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.omnibecard.common.apiPayload.code.BaseErrorCode;
import org.example.omnibecard.common.apiPayload.code.ErrorReasonDto;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 일반 상태
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"COMMON500","서버 에러"),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다"),
    _FORBIDDEN(HttpStatus.FORBIDDEN,"COMMON402","금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND,"COMMON403","데이터를 찾지 못했습니다."),

    // member 상태
    _NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST,"MEMBER4001","비밀번호가 동일하지 않습니다."),
    _LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "MEMBER4002", "로그인에 실패했습니다."),
    _ALREADY_EXIST_LOGINID(HttpStatus.BAD_REQUEST,"MEMBER4003","이미 존재하는 아이디입니다."),
    _NOT_FOUND_LOGINID(HttpStatus.NOT_FOUND,"MEMBER4004","없는 아이디입니다."),
    _NOT_MATCH_NEWPASSWORD(HttpStatus.BAD_REQUEST,"MEMBER4005","새로운 비밀번호가 동일하지 않습니다."),
    _DUPLICATE_PASSWORD(HttpStatus.BAD_REQUEST,"MEMBER4006","기존 비밀번호와 동일합니다."),
    _ALREADY_RESIGN(HttpStatus.BAD_REQUEST,"MEMBER4007","이미 탈퇴한 사용자입니다."),

    // 카드 상태
    _CARD_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"CARD4001","이미 존재하는 사용자입니다."),
    _CARD_NUMBER_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"CARD4002","중복된 카드 번호로 인한 생성 실패입니다."),
    _NOT_FOUND_CARD(HttpStatus.NOT_FOUND,"CARD4003","사용자의 카드가 없습니다."),
    _NOT_MATCH_CARDPASSWORD(HttpStatus.BAD_REQUEST,"CARD4004","카드비밀번호가 일치하지 않습니다."),

    // 서비스 상태
    _USER_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"SERVICE5000","USER 서버 에러"),

    // DB 상태
    _DATABASE_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"DATABASE4001","데이터베이스 저장 오류입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .code(code)
                .message(message)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .code(code)
                .message(message)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
