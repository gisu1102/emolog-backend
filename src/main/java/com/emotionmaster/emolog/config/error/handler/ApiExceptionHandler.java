package com.emotionmaster.emolog.config.error.handler;

import com.emotionmaster.emolog.config.error.exception.DiaryException;
import com.emotionmaster.emolog.config.error.exception.UserException;
import com.emotionmaster.emolog.config.error.response.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiErrorResponse> handleUserException(UserException e) {
        return ApiErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(DiaryException.class)
    public ResponseEntity<ApiErrorResponse> handleDiaryException(DiaryException e){
        return ApiErrorResponse.toResponseEntity(e.getErrorCode());
    }

}