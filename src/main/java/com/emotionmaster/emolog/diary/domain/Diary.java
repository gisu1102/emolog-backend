package com.emotionmaster.emolog.diary.domain;

import com.emotionmaster.emolog.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="diary_date", nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String content;


    @Builder
    public Diary(LocalDate date, String content) {
        this.date = date;
        this.content = content;
    }
}
