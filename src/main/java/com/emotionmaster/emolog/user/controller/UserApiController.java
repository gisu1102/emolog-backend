package com.emotionmaster.emolog.user.controller;


import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.diary.dto.request.AddDiaryRequest;
import com.emotionmaster.emolog.diary.dto.response.AddDiaryResponse;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.dto.request.UserRequestDto;
import com.emotionmaster.emolog.user.dto.response.UserInfoResponseDto;
import com.emotionmaster.emolog.user.dto.response.UserResponseDto;
import com.emotionmaster.emolog.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "oauthLogin";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    //회원 정보 무엇을 수정할지 정하기
    @PutMapping("/api/updateUser/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id , @RequestBody UserRequestDto request){
        UserResponseDto updatedUser = userService.update(id,request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(updatedUser);
    }

    @GetMapping("/api/userInfo/{id}")
    public ResponseEntity<UserInfoResponseDto> userInfo(@PathVariable Long id){
        UserInfoResponseDto infoUser = userService.info(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(infoUser);
    }

    @DeleteMapping("/api/userDelete/{id}")
    public ResponseEntity<Void> userDelete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.ok()
                .build();
    }


}

