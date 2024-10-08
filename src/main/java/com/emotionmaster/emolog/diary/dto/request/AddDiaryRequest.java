package com.emotionmaster.emolog.diary.dto.request;

import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.emotion.domain.Emotion;
import com.emotionmaster.emolog.q_a.domain.Q_A;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddDiaryRequest {
    private LocalDate date;
    private Qa q_a;
    private String emotion;
    private String content;
    private String url;

    public Diary toDiaryEntity(User user){
        return Diary.builder()
                .content(content)
                .date(date)
                .user(user)
                .build();
    }
    public Q_A toQ_AEntity(Diary diary){
        return Q_A.builder()
                .question(q_a.question())
                .answer(q_a.answer())
                .diary(diary)
                .build();
    }

    public Emotion toEmotionEntity(Diary diary){
        return Emotion.builder()
                .emotion(emotion)
                .diary(diary)
                .build();
    }
}

