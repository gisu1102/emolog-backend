package com.emotionmaster.emolog.config.error.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getError();
    HttpStatus getHttpStatus();
    String getMessage();
}
