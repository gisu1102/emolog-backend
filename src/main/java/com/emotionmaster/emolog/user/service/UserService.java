package com.emotionmaster.emolog.user.service;

import com.emotionmaster.emolog.config.TokenAuthenticationFilter;
import com.emotionmaster.emolog.config.auth.providerOauthUser.ProviderOAuth2UserKakao;
import com.emotionmaster.emolog.config.error.errorcode.UserErrorcode;
import com.emotionmaster.emolog.config.error.exception.UserException;
import com.emotionmaster.emolog.config.jwt.TokenProvider;
import com.emotionmaster.emolog.diary.repository.DiaryRepository;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.dto.request.UserUpdateRequestDto;
import com.emotionmaster.emolog.user.dto.response.KakaoResponseDto;
import com.emotionmaster.emolog.user.dto.response.MyPageResponseDto;
import com.emotionmaster.emolog.user.dto.response.UserInfoResponseDto;
import com.emotionmaster.emolog.user.dto.response.UserUpdateResponseDto;
import com.emotionmaster.emolog.user.repository.RefreshTokenRepository;
import com.emotionmaster.emolog.user.repository.UserRepository;
import com.emotionmaster.emolog.util.UserUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.emotionmaster.emolog.config.auth.OAuth2SuccessHandler.ACCESS_TOKEN_DURATION;
import static com.emotionmaster.emolog.config.auth.OAuth2SuccessHandler.REFRESH_TOKEN_DURATION;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final HttpServletRequest request;

    //restTemplate 주입
    RestTemplate restTemplate = new RestTemplate();

    //구글 로그아웃 url
    String googleLogoutUrl = "https://accounts.google.com/Logout";
    String kakaoLogoutUrl ="https://kapi.kakao.com/v1/user/logout";

    public User getCurrentUser(){
        String token = tokenAuthenticationFilter.getAccessToken(request);
        long userId = tokenProvider.getUserId(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorcode.MEMBER_NOT_FOUND));
    }

    /*
    OAuth2 로그아웃
     */
    public Map<String, String> logout(HttpServletResponse response, String accessToken ) {

        User user = getCurrentUser();

        //각 플랫폼 별 로그아웃 엔드 포인트 (플랫폼 세션에서 제거)
        // 같은 아이디로 다른 애플리케이션에서 접속 했을시 재인증
        //네이버의 경우 로그아웃 api 지원 x
        try {
            switch (user.getOauthType()) {
                case "google":
                    restTemplate.getForObject(googleLogoutUrl, String.class);
                    break;
                case "kakao":
                    restTemplate.postForObject(kakaoLogoutUrl, null, String.class, "Bearer " + accessToken);
                    break;
                case "naver":
                    // 네이버의 경우, 엔드포인트 호출 없이 쿠키만 삭제
                    break;
                default:
                    throw new UserException(UserErrorcode.OAUTH_LOGIN_ERROR);
            }
        } catch (Exception e) {
            // 로그아웃 실패 처리 (예: 로그 기록)
            e.printStackTrace();
        }

        //클라이언트 측 쿠키 제거 (Null -> 빈 문자열)
        //모호성 해결 - null 인경우 이름 설정에 오류가 있음을 나타낼수도 있음
        //http 표준 규격
        Cookie cookie = new Cookie("access_token" , "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 쿠키 만료
        response.addCookie(cookie);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Logout successful");
        return responseBody;

    }


    /*
    회원 정보 업데이트
     */
    @Transactional
    public UserUpdateResponseDto update(UserUpdateRequestDto dto) {
        User user = getCurrentUser();
        //builder -> toBuilder (객체 수정 )
        user.toUpdateUser(dto.getNickname(), dto.getAge());
        return new UserUpdateResponseDto(user) ;
    }


    /*
    회원 정보 조회
     */
    //user 정보 넘기기
    public UserInfoResponseDto info() {
        return new UserInfoResponseDto(getCurrentUser());
    }

    /*
    회원 탈퇴
     */
    @Transactional
    public void delete() {
        userRepository.delete(getCurrentUser());
    }

    /*
    마이페이지 화면
    - 닉네임
    - 이번달(Month) 작성한 일기 개수
    - 총 색깔 개수
     */
    public MyPageResponseDto getMyPageInfo() {
        User user = getCurrentUser();
        return MyPageResponseDto.builder()
                .nickname(user.getNickname())
                .diaryCount(diaryRepository.getTheNumberOfDiariesForThisMonth(user.getId()))
                .colorCount(diaryRepository.getTheNumberOfColors(user.getId()))
                .build();
    }

    /*
    카카오 로그인 정보 로그인
    -> AccessToken, RefreshToken 발급
    Access : Response
    Refresh : Cookie 저장
     */
    public KakaoResponseDto login(HttpServletRequest request, HttpServletResponse response, Map<String, Object> attributes) {
        ProviderOAuth2UserKakao kakaoUser = new ProviderOAuth2UserKakao(attributes);
        Optional<User> user = userRepository.findByEmail(kakaoUser.getEmail());
        if (user.isEmpty()) {
            User savedUser = userRepository.save(User.builder()
                    .email(kakaoUser.getEmail())
                    .name(kakaoUser.getName())
                    .oauthType(kakaoUser.getProvider())
                    .build());
            return new KakaoResponseDto(savedUser, generateToken(request, response, savedUser));
        }
        return new KakaoResponseDto(user.get(), generateToken(request, response, user.get()));

    }

    public String generateToken(HttpServletRequest request, HttpServletResponse response, User user){
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        UserUtil.saveRefreshToken(refreshTokenRepository, user.getId(), refreshToken);
        UserUtil.addRefreshTokenToCookie(request, response, refreshToken);

        return tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
    }
}