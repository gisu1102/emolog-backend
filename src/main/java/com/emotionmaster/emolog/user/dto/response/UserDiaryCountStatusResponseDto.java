package com.emotionmaster.emolog.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter

public class UserDiaryCountStatusResponseDto {
    private long diaryCount;
    private long colorCount;

    @Builder
    public UserDiaryCountStatusResponseDto(long diaryCount, long colorCount) {
        this.diaryCount = diaryCount;
        this.colorCount = colorCount;
    }

}
