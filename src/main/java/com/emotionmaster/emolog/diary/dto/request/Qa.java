package com.emotionmaster.emolog.diary.dto.request;

import lombok.Builder;

@Builder
public record Qa(String question, String answer) {

}
