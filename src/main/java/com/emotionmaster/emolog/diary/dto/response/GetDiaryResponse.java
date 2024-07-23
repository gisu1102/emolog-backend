package com.emotionmaster.emolog.diary.dto.response;

import com.emotionmaster.emolog.diary.domain.Diary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetDiaryResponse extends SummaryDiaryResponse{

    private Q_A q_a;

    @Override
    public void toSummary(Diary diary) {
        super.toSummary(diary);
        this.q_a = new Q_A(diary.getQ_a().getQuestion(),
                diary.getQ_a().getAnswer());
    }
}

record Q_A (String question, String answer){

}