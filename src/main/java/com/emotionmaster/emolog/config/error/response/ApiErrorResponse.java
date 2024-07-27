package com.emotionmaster.emolog.config.error.response;

import com.emotionmaster.emolog.config.error.errorcode.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class ApiErrorResponse {
    private int status;
    private String error;
    private String message;

    @Builder
    public ApiErrorResponse(int status, String error, String message) {
        super();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public static ResponseEntity<ApiErrorResponse> toResponseEntity(ErrorCode e){
        return ResponseEntity.status(e.getHttpStatus())
                .body(ApiErrorResponse.builder()
                        .status(e.getHttpStatus().value())
                        .error(e.getError())
                        .message(e.getMessage())
                        .build());
    }
}