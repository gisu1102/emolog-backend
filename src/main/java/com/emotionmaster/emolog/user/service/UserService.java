package com.emotionmaster.emolog.user.service;

import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.dto.request.UserRequestDto;
import com.emotionmaster.emolog.user.dto.response.UserInfoResponseDto;
import com.emotionmaster.emolog.user.dto.response.UserResponseDto;
import com.emotionmaster.emolog.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    //restTemplate 주입
    RestTemplate restTemplate = new RestTemplate();

    //구글 로그아웃 url
    String googleLogoutUrl = "https://accounts.google.com/Logout";
    String kakaoLogoutUrl ="https://kapi.kakao.com/v1/user/logout";
    public Long save(UserRequestDto dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }


    public Map<String, String> logout(HttpServletResponse response, Long id ,String accessToken ) {

        User user = findById(id);

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
                    throw new IllegalArgumentException("Unsupported provider: " + user.getOauthType());
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

    public UserResponseDto update(Long id, UserRequestDto dto) {
        User user = findById(id);
        //builder -> toBuilder (객체 수정 )
        User updatedUser= user.toBuilder()
                .nickname(dto.getNickname())
                .age(dto.getAge())
                .build();
        userRepository.save(updatedUser);
        return new UserResponseDto(updatedUser) ;
    }


    //user 정보 넘기기
    public UserInfoResponseDto info(Long id) {
        User user = findById(id);
        return new UserInfoResponseDto(user);
    }

    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }





    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}