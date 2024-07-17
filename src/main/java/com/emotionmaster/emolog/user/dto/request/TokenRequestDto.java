package com.emotionmaster.emolog.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequestDto {
    private String refreshToken;
}