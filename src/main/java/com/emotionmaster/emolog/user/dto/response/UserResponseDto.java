package com.emotionmaster.emolog.user.dto.response;

import com.emotionmaster.emolog.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private String nickname;
    private int age;

    public UserResponseDto(User user){
        this.nickname = user.getNickname();
        this.age = user.getAge();
    }

}
