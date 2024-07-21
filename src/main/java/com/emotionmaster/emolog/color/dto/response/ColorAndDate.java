package com.emotionmaster.emolog.color.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
// 월간, 주간의 색을 날짜와 같이 반환해주는 DTO
public class ColorAndDate {
    private LocalDate date;
    private String hexa;
    private DayOfWeek dayOfWeek;

    @Builder
    public ColorAndDate(LocalDate date, String hexa, DayOfWeek dayOfWeek) {
        this.date = date;
        this.hexa = hexa;
        this.dayOfWeek = dayOfWeek;
    }
}
