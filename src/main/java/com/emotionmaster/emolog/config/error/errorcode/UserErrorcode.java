package com.emotionmaster.emolog.config.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorcode implements ErrorCode{

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.", "User-100"),
    MEMBER_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다.", "User-101"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", "User-102"),
    OAUTH_LOGIN_ERROR(HttpStatus.BAD_REQUEST, "OAuth 로그인 중 오류 발생", "User-103"),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token이 유효하지 않습니다.", "Token-100"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다", "Token-101"),
    UNSUPPORTED_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "지원되지 않는 토큰입니다", "Token-102"),
    ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 올바른 형식이 아니거나 claim이 비어 있습니다.", "Token-103"),
    REFRESH_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "해당 RefreshToken에 맞는 회원이 존재하지 않습니다.", "Token-104"),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다.", "Token-105");

    private final HttpStatus httpStatus;
    private final String message;
    private final String error;
}
