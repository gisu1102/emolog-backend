package com.emotionmaster.emolog.user.controller;


import com.emotionmaster.emolog.config.auth.providerOauthUser.ProviderOAuth2UserGoogle;
import com.emotionmaster.emolog.config.auth.providerOauthUser.ProviderOAuth2UserKakao;
import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.diary.dto.request.AddDiaryRequest;
import com.emotionmaster.emolog.diary.dto.response.AddDiaryResponse;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.dto.request.UserRequestDto;
import com.emotionmaster.emolog.user.dto.response.UserInfoResponseDto;
import com.emotionmaster.emolog.user.dto.response.UserResponseDto;
import com.emotionmaster.emolog.user.service.TokenService;
import com.emotionmaster.emolog.user.service.UserInfoService;
import com.emotionmaster.emolog.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final UserInfoService userInfoService;
    private final TokenService tokenService;

    private final ProviderOAuth2UserGoogle providerOAuth2UserGoogle;
    private final ProviderOAuth2UserKakao providerOAuth2UserKakao;

//    @GetMapping("/google")
//    public void loginGoogle(HttpServletResponse response) throws IOException {
//        response.sendRedirect(providerOAuth2UserGoogle.responseUrl());
//        log.info("googleLoginStart: ");
//    }
//
//    @GetMapping("/kakao")
//    public void loginKakao(HttpServletResponse response) throws IOException {
//        response.sendRedirect(providerOAuth2UserKakao.responseUrl());
//        log.info("kakaoLoginStart: ");
//    }
//
//    @GetMapping("/login/oauth2/code/google")
//    public void handleGoogleCallback(@RequestParam(value = "code") String code) throws IOException {
//        log.info("Response Body: " + code);
//        // 인가 코드를 사용해 액세스 토큰을 요청
//        String accessToken = tokenService.exchangeCodeForToken(code);
//        // 액세스 토큰을 사용해 사용자 정보를 요청
//        String userInfo = userInfoService.getUserInfo(accessToken);
//
//        log.info("구글 api 서버 코드: " + code);
//    }
//
//    @GetMapping("/login/oauth2/code/kakao")
//    public String hadnleKakaoCallback(@RequestParam(value = "code") String code) {
//        return "code" + code;
//    }


    @PostMapping("/logout/{id}")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response , @PathVariable Long id , @RequestHeader("Authorization") String accessToken) {
        Map<String, String> result = userService.logout(response, id , accessToken);
        return ResponseEntity.ok(result);
    }
    //회원 정보 무엇을 수정할지 정하기
    @PutMapping("/api/updateUser/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id , @RequestBody UserRequestDto request){
        UserResponseDto updatedUser = userService.update(id,request);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/api/userInfo/{id}")
    public ResponseEntity<UserInfoResponseDto> userInfo(@PathVariable Long id){
        UserInfoResponseDto infoUser = userService.info(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(infoUser);
    }

    @DeleteMapping("/api/userDelete/{id}")
    public ResponseEntity<Void> userDelete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent()
                .build();
    }


}

