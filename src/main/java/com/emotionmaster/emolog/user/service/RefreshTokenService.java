package com.emotionmaster.emolog.user.service;


import com.emotionmaster.emolog.config.error.errorcode.UserErrorcode;
import com.emotionmaster.emolog.config.error.exception.UserException;
import com.emotionmaster.emolog.config.jwt.TokenProvider;
import com.emotionmaster.emolog.user.domain.RefreshToken;
import com.emotionmaster.emolog.user.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new UserException(UserErrorcode.REFRESH_TOKEN_ERROR));
    }


    //로그아웃시 인증정보 바탕으로 해당 아이디 리프레시 토큰 삭제
    @Transactional
    public void delete() {
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        Long userId = tokenProvider.getUserId(token);

        refreshTokenRepository.deleteByUserId(userId);
    }
}