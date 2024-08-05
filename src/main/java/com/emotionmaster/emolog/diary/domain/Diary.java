package com.emotionmaster.emolog.diary.domain;

import com.emotionmaster.emolog.color.domain.Color;
import com.emotionmaster.emolog.comment.domain.Comment;
import com.emotionmaster.emolog.diary.dto.request.AddDiaryRequest;
import com.emotionmaster.emolog.emotion.domain.Emotion;
import com.emotionmaster.emolog.config.BaseEntity;
import com.emotionmaster.emolog.image.domain.Image;
import com.emotionmaster.emolog.q_a.domain.Q_A;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.util.DateUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="diary_date", nullable = false)
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "varchar(2000)")
    private String content;

    //몇 주인가?
    @Column(name="date_week")
    private Integer week;

    //몇 요일인가?
    @Column(name="day_of_week")
    private DayOfWeek dayOfWeek;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //mappedBy는 누가 이 객체를 관리할 것인지 정의 하는 것으로 !정의 되지 않은 객체가 주인이 된다!
    // 여기서는 모두 diary 객체가 주인이 아니다!!! Diary 객체에서는 조회만 가능, 수정 불가
    // 얘랑 쟤랑 묶였구나~~ 를 명시해주는 것이라고 생각하면 된다.

    //cascade는 부모의 영속성 상태를 자식에게 전이하겠다.
    //CascadeType.REMOVE 는 부모가 지워질 때, 자식들도 같이 지워진다는 것

    @ManyToOne()
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @OneToOne(mappedBy = "diary", cascade = CascadeType.REMOVE)
    private Q_A q_a;

    @OneToOne(mappedBy = "diary", cascade = CascadeType.REMOVE)
    private Emotion emotion;

    @OneToOne(mappedBy = "diary", cascade = CascadeType.REMOVE)
    private Color color;

    @OneToOne(mappedBy = "diary" , cascade = CascadeType.REMOVE)
    private Image image;

    @Builder
    public Diary(LocalDate date, String content, Integer week, DayOfWeek dayOfWeek, User user) {
        this.date = date;
        this.content = content;
        this.week = week;
        this.dayOfWeek = dayOfWeek;
        this.user = user;
    }

    public Diary toUploadDiary(AddDiaryRequest request){
        this.date = request.getDate();
        this.week = DateUtil.getWeekOfMonthByDate(date);
        this.dayOfWeek = date.getDayOfWeek();

        return this;
    }

    public void toComment(Comment comment){
        this.comment = comment;
    }
}
