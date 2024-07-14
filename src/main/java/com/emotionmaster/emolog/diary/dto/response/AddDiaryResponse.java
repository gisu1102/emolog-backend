package com.emotionmaster.emolog.diary.dto.response;

import com.emotionmaster.emolog.diary.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
public class AddDiaryResponse {
    private final Long id;
    private final LocalDate date;
    private final String content;

    public AddDiaryResponse(Diary diary){
        this.id = diary.getId();
        this.date = diary.getDate();
        this.content = diary.getContent();
    }
}
