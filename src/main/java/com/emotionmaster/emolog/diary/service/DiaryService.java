package com.emotionmaster.emolog.diary.service;

import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.diary.dto.request.AddDiaryRequest;
import com.emotionmaster.emolog.diary.repository.DiaryRepository;
import com.emotionmaster.emolog.emotion.repository.EmotionRepository;
import com.emotionmaster.emolog.q_a.repository.QaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final QaRepository qaRepository;
    private final EmotionRepository emotionRepository;
    private final DiaryRepository diaryRepository;

    public Diary save(AddDiaryRequest request){
        Diary diary = diaryRepository.save(request.toDiaryEntity());
        qaRepository.save(request.toQ_AEntity(diary));
        emotionRepository.save(request.toEmotionEntity(diary));
        return diary;
    }

    public void delete(long id){
        diaryRepository.deleteById(id);
    }

    public List<Diary> findAllColorOfMonth(int month){
        return diaryRepository.findAllByMonth(month);
    }
}
