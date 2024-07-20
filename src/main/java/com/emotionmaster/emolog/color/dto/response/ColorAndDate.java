package com.emotionmaster.emolog.color.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ColorAndDate {
    private LocalDate date;
    private String hexa;

    public ColorAndDate(LocalDate date, String hexa) {
        this.date = date;
        this.hexa = hexa;
    }
}
