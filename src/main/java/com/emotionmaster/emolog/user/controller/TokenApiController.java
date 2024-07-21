package com.emotionmaster.emolog.user.controller;

import com.emotionmaster.emolog.user.dto.request.TokenRequestDto;
import com.emotionmaster.emolog.user.dto.response.TokenResponseDto;
import com.emotionmaster.emolog.user.service.RefreshTokenService;
import com.emotionmaster.emolog.user.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {

    private final TokenService tokenService;

    private final RefreshTokenService refreshTokenService;
    @PostMapping("/api/token")
    public ResponseEntity<TokenResponseDto> createNewAccessToken(@RequestBody TokenRequestDto request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TokenResponseDto(newAccessToken));
    }


    @DeleteMapping("/api/refresh-token")
    public ResponseEntity deleteRefreshToken() {
        refreshTokenService.delete();
        return ResponseEntity.ok()
                .build();
    }
}