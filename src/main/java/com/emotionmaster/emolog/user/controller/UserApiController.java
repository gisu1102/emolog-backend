package com.emotionmaster.emolog.user.controller;


import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.diary.dto.request.AddDiaryRequest;
import com.emotionmaster.emolog.diary.dto.response.AddDiaryResponse;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.dto.request.UserRequestDto;
import com.emotionmaster.emolog.user.dto.response.UserInfoResponseDto;
import com.emotionmaster.emolog.user.dto.response.UserResponseDto;
import com.emotionmaster.emolog.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "oauthLogin";
    }

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

