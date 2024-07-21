package com.emotionmaster.emolog.user.service;

import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.dto.request.UserRequestDto;
import com.emotionmaster.emolog.user.dto.response.UserInfoResponseDto;
import com.emotionmaster.emolog.user.dto.response.UserResponseDto;
import com.emotionmaster.emolog.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public Long save(UserRequestDto dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }


    public Map<String, String> logout(HttpServletResponse response) {

        //클라이언트 측 쿠키 제거
        Cookie cookie = new Cookie("access_token" , null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 쿠키 만료
        response.addCookie(cookie);

        //구글 로그아웃 엔드 포인트 (세션에서 구글 로그인 제거)
        try {
            //get 요청 보낸후 응답 본문 문자열로 받음
            restTemplate.getForObject(googleLogoutUrl, String.class);
        } catch (Exception e) {
            // 로그아웃 실패 처리
        }

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