package com.emotionmaster.emolog.util;

import com.emotionmaster.emolog.user.domain.RefreshToken;
import com.emotionmaster.emolog.user.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import static com.emotionmaster.emolog.config.auth.OAuth2SuccessHandler.REFRESH_TOKEN_COOKIE_NAME;
import static com.emotionmaster.emolog.config.auth.OAuth2SuccessHandler.REFRESH_TOKEN_DURATION;

@RequiredArgsConstructor
public class UserUtil {

    public static void saveRefreshToken(RefreshTokenRepository refreshTokenRepository, Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    //RefreshToken 삭제후 새로운
    public static void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }
}
