package com.emotionmaster.emolog.color.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddColorRequest {
    private LocalDate date;
    private String emotion;
}
