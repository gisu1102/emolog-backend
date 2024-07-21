package com.emotionmaster.emolog.diary.dto.response;

import com.emotionmaster.emolog.color.dto.response.ColorAndDate;
import com.emotionmaster.emolog.util.DateUtil;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SummaryDiaryResponse {
    private Diary diary;

    private Color color;

    private List<String> emotion;

    private String comment;

    public void toSummary(com.emotionmaster.emolog.diary.domain.Diary diary){
        this.diary = Diary.builder()
                .date(diary.getDate())
                .content(diary.getContent())
                .dayOfWeek(diary.getDayOfWeek())
                .build();
        this.color = new Color(diary.getColor().getHexa());
        this.emotion = List.of(diary.getEmotion().getEmotion().split(",")); //todo 대표 감정?
        this.comment = diary.getComment().getComment();
    }
}

record Color (String hexa){}

@Builder
record Diary (LocalDate date, DayOfWeek dayOfWeek, String content){}
