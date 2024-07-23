package com.emotionmaster.emolog.config.auth;

import com.emotionmaster.emolog.config.auth.providerOauthUser.ProviderOAuth2UserCustom;
import com.emotionmaster.emolog.config.jwt.TokenProvider;
import com.emotionmaster.emolog.user.domain.RefreshToken;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.repository.RefreshTokenRepository;
import com.emotionmaster.emolog.user.service.UserService;
import com.emotionmaster.emolog.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;


@RequiredArgsConstructor
@Component

//OAuth2 로그인 성공 이후 로직
//RefreshToken 생성 -> 쿠키저장 -> 리디렉션
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

    //추후 재설정 리다이렉트 경로 로그인 성공시
    public static final String REDIRECT_PATH = "/";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        log.info("successHandler");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("OAuth2User: {}", oAuth2User);

        // 제공자 정보를 추출
        String providerId = getProviderIdFromOAuth2User(oAuth2User);
        log.info("Provider ID: {}", providerId);

        ProviderOAuth2UserCustom oAuth2UserInfo = ProviderOAuth2UserCustom.create(oAuth2User, providerId);
        String email = oAuth2UserInfo.getEmail();
        log.info("Retrieved email from OAuth2User: {}", email);

        User user = userService.findByEmail(email);


        //RefreshToken 생성 및 쿠키에 추가
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        //AccessToken 생성
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        //해당 경로에 AcessToken 추가
        String targetUrl = getTargetUrl(accessToken);
        //인증 관련 설정쿠키 제거
        clearAuthenticationAttributes(request, response);
        //리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }



    //Refresh 토큰 저장
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    //RefreshToken 삭제후 새로운
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    //RefreshToken 삭제
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }

    private String getProviderIdFromOAuth2User(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if (attributes.containsKey("sub")) {
            return "google";
        } else if (attributes.containsKey("id") && attributes.containsKey("kakao_account")) {
            return "kakao";
        } else if (attributes.containsKey("id") && attributes.containsKey("email")) {
            return "naver";
        } else {
            throw new IllegalArgumentException("Unsupported provider");
        }
    }
}
