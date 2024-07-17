package com.emotionmaster.emolog.color.dto.response;

import com.emotionmaster.emolog.emotion.domain.EmotionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColorResponse {
    private int red;
    private int blue;
    private int green;
    private EmotionType type;
}
