package com.emotionmaster.emolog.diary.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SummaryDiaryResponse {
    protected Diary diary;

    protected Color color;

    protected List<String> emotion;

    protected String comment;

    //todo 사진 추가

    public void toSummary(com.emotionmaster.emolog.diary.domain.Diary diary){
        this.diary = Diary.builder()
                .date(diary.getDate())
                .content(diary.getContent())
                .dayOfWeek(diary.getDayOfWeek())
                .build();
        this.color = Color.builder()
                .hexa(diary.getColor().getHexa())
                .red(diary.getColor().getRed())
                .green(diary.getColor().getGreen())
                .blue(diary.getColor().getBlue())
                .build();
        this.emotion = List.of(diary.getEmotion().getEmotion().split("/"));
        this.comment = diary.getComment().getComment();
    }
}

@Builder
record Color (String hexa, int red, int green, int blue){}

@Builder
record Diary (LocalDate date, DayOfWeek dayOfWeek, String content){}
