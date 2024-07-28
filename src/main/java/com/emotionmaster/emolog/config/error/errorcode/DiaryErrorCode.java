package com.emotionmaster.emolog.config.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DiaryErrorCode implements ErrorCode{

    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일기가 존재하지 않습니다.", "Diary-202"),
    DIARY_DUPLICATED(HttpStatus.BAD_REQUEST, "해당 날짜에 일기가 이미 존재합니다.", "Diary-201"),
    DIARY_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "해당 일기에 접근 권한이 없습니다.", "Diary-203"),
    IMAGE_SAVE_ERROR(HttpStatus.BAD_REQUEST, "이미지 저장 중 오류 발생", "Diary-204"),
    API_BAD_REQUEST(HttpStatus.BAD_REQUEST, "API 연결 중 오류 발생", "Diary-205");



    private final HttpStatus httpStatus;
    private final String message;
    private final String error;

}
