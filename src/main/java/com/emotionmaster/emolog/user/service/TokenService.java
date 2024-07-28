package com.emotionmaster.emolog.user.service;

import com.emotionmaster.emolog.config.TokenAuthenticationFilter;
import com.emotionmaster.emolog.config.error.errorcode.UserErrorcode;
import com.emotionmaster.emolog.config.error.exception.UserException;
import com.emotionmaster.emolog.config.jwt.TokenProvider;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validToken(refreshToken)) {
            throw new UserException(UserErrorcode.INVALID_TOKEN);
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorcode.MEMBER_NOT_FOUND));

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }


}
