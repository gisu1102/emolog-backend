package com.emotionmaster.emolog.emotion.domain;

import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.config.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Emotion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(2000)")
    private String emotion;

    @Column(name = "emotion_type")
    private EmotionType emotionType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Builder
    public Emotion(String emotion, Diary diary) {
        this.emotion = emotion;
        this.diary = diary;
    }

    public void toEmotionType(EmotionType emotionType){
        this.emotionType = emotionType;
    }
}
