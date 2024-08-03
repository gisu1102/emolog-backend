package com.emotionmaster.emolog.image.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageResponse {
    private String url;

    public ImageResponse(String url) {
        this.url = url;
    }
}
