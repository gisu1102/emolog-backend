package com.emotionmaster.emolog.user.controller;


import com.emotionmaster.emolog.config.auth.providerOauthUser.ProviderOAuth2UserGoogle;
import com.emotionmaster.emolog.config.auth.providerOauthUser.ProviderOAuth2UserKakao;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.dto.request.UserRequestDto;
import com.emotionmaster.emolog.user.dto.response.UserDiaryCountStatusResponseDto;
import com.emotionmaster.emolog.user.dto.response.UserInfoResponseDto;
import com.emotionmaster.emolog.user.dto.response.UserResponseDto;
import com.emotionmaster.emolog.user.service.TokenService;
import com.emotionmaster.emolog.user.service.UserService;
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
public class UserApiController {

    private final UserService userService;

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


    //이번 달 일기, 색 개수 조회
    @GetMapping("/api/userDiaryColorCount/{id}")
    public ResponseEntity<UserDiaryCountStatusResponseDto> countUserDiaryAndColorByMonth(@PathVariable Long id) {
        UserDiaryCountStatusResponseDto response = userService.getUserDiaryStats(id);
        log.info("일기 개수+ 색" + response);
        return ResponseEntity.ok(response);
    }


}

