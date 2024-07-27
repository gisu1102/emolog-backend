package com.emotionmaster.emolog.config.error.exception;

import com.emotionmaster.emolog.config.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserException extends RuntimeException {
    private final ErrorCode errorCode;
}
