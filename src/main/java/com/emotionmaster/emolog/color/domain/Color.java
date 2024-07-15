package com.emotionmaster.emolog.color.domain;

import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.global.BaseEntity;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Color extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int red;

    @Column(nullable = false)
    private int green;

    @Column(nullable = false)
    private int blue;

    @Column(name="hexa", nullable = false)
    private String hexa;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Builder
    public Color(int red, int green, int blue, String hexa, Diary diary) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.hexa = hexa;
        this.diary = diary;
    }
}
