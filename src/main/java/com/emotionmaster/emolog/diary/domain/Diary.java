package com.emotionmaster.emolog.diary.domain;

import com.emotionmaster.emolog.color.domain.Color;
import com.emotionmaster.emolog.emotion.domain.Emotion;
import com.emotionmaster.emolog.config.BaseEntity;
import com.emotionmaster.emolog.q_a.domain.Q_A;
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

    //mappedBy는 누가 이 객체를 관리할 것인지 정의 하는 것으로 !정의 되지 않은 객체가 주인이 된다!
    // 여기서는 모두 diary 객체가 주인이 아니다!!! Diary 객체에서는 조회만 가능, 수정 불가
    // 얘랑 쟤랑 묶였구나~~ 를 명시해주는 것이라고 생각하면 된다.

    //cascade는 부모의 영속성 상태를 자식에게 전이하겠다.
    //CascadeType.REMOVE 는 부모가 지워질 때, 자식들도 같이 지워진다는 것

    @OneToOne(mappedBy = "diary", cascade = CascadeType.REMOVE)
    private Q_A q_a;

    @OneToOne(mappedBy = "diary", cascade = CascadeType.REMOVE)
    private Emotion emotion;

    @OneToOne(mappedBy = "diary", cascade = CascadeType.REMOVE)
    private Color color;

    @Builder
    public Diary(LocalDate date, String content) {
        this.date = date;
        this.content = content;
    }
}
