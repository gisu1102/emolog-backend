package com.emotionmaster.emolog.user.dto.response;

import com.emotionmaster.emolog.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponseDto {
    private String email;
    private String name;
    private String oauthType;
    private String nickname;
    private int age;

    public UserInfoResponseDto(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.oauthType = user.getOauthType();
        this.nickname = user.getNickname();
        this.age = user.getAge();
    }
}
