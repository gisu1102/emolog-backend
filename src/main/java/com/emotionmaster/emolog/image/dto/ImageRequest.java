package com.emotionmaster.emolog.image.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ImageRequest {
    private String content;
    private LocalDate date;
    private String hexacode;

    @Builder
    public ImageRequest(String content, LocalDate date, String hexacode) {
        this.content = content;
        this.date = date;
        this.hexacode = hexacode;
    }
}
