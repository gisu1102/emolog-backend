package com.emotionmaster.emolog.user.controller;


import com.emotionmaster.emolog.user.dto.request.UserUpdateRequestDto;
import com.emotionmaster.emolog.user.dto.response.KakaoResponseDto;
import com.emotionmaster.emolog.user.dto.response.MyPageResponseDto;
import com.emotionmaster.emolog.user.dto.response.UserInfoResponseDto;
import com.emotionmaster.emolog.user.dto.response.UserUpdateResponseDto;
import com.emotionmaster.emolog.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;

    @PostMapping("/kakao/login")
    public ResponseEntity<KakaoResponseDto> kakaoLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> attributes){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.login(request, response, attributes));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response , @RequestHeader("Authorization") String accessToken) {
        Map<String, String> result = userService.logout(response , accessToken);
        return ResponseEntity.ok()
                .body(result);
    }

    /*
    1. 로그인 후 age, nickname 값을 추가로 입력받아 업데이트
    2. 회원 정보 수정
    - Header로 들어오는 token 값으로 회원 조회
     */
    @PutMapping("/user")
    public ResponseEntity<UserUpdateResponseDto> updateUser(@RequestBody UserUpdateRequestDto request){
        UserUpdateResponseDto updatedUser = userService.update(request);
        return ResponseEntity.ok()
                .body(updatedUser);
    }

    /*
    회원 정보 조회
    - Header로 들어오는 token 값으로 회원 조회
     */
    @GetMapping("/user")
    public ResponseEntity<UserInfoResponseDto> userInfo(){
        UserInfoResponseDto infoUser = userService.info();
        return ResponseEntity.ok()
                .body(infoUser);
    }

    @DeleteMapping("/user")
    public ResponseEntity<Void> userDelete(){
        userService.delete();
        return ResponseEntity.noContent()
                .build();
    }


    /*
    마이페이지 화면
    - 닉네임
    - 이번달(Month) 작성한 일기 개수
    - 총 색깔 개수
     */
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponseDto> getMyPageInfo() {
        MyPageResponseDto response = userService.getMyPageInfo();
        log.info("일기 개수+ 색" + response);
        return ResponseEntity.ok()
                .body(response);
    }


}

