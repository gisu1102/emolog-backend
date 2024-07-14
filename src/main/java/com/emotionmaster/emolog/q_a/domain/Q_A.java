package com.emotionmaster.emolog.q_a.domain;

import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Q_A extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    @OneToOne
    private Diary diary;

    @Builder
    public Q_A(String question, String answer, Diary diary) {
        this.question = question;
        this.answer = answer;
        this.diary = diary;
    }
}
