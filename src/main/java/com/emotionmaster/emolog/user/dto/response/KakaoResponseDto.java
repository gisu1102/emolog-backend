package com.emotionmaster.emolog.user.dto.response;

import com.emotionmaster.emolog.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoResponseDto {
    private String email;
    private String name;
    private String accessToken;
    private boolean isNew;

    @Builder
    public KakaoResponseDto(User user, String accessToken, boolean isNew) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.accessToken = accessToken;
        this.isNew = isNew;

    }
}
