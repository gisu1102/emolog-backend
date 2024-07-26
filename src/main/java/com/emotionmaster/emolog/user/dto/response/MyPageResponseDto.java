package com.emotionmaster.emolog.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageResponseDto {
    private String nickname;
    private long diaryCount;
    private long colorCount;

    @Builder
    public MyPageResponseDto(String nickname, long diaryCount, long colorCount) {
        this.nickname = nickname;
        this.diaryCount = diaryCount;
        this.colorCount = colorCount;
    }
}
