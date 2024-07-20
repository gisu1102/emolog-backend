package com.emotionmaster.emolog.diary.service;

import com.emotionmaster.emolog.color.service.ColorService;
import com.emotionmaster.emolog.comment.service.CommentService;
import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.diary.dto.request.AddDiaryRequest;
import com.emotionmaster.emolog.diary.repository.DiaryRepository;
import com.emotionmaster.emolog.emotion.domain.Emotion;
import com.emotionmaster.emolog.emotion.domain.EmotionType;
import com.emotionmaster.emolog.emotion.repository.EmotionRepository;
import com.emotionmaster.emolog.q_a.repository.QaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final QaRepository qaRepository;
    private final EmotionRepository emotionRepository;
    private final DiaryRepository diaryRepository;

    private final ColorService colorService;
    private final CommentService commentService;


    @Transactional
    public Diary save(AddDiaryRequest request){
        Diary diary = diaryRepository.save(request.toDiaryEntity());
        qaRepository.save(request.toQ_AEntity(diary)); //Q_A 저장
        EmotionType emotionType = colorService.save(request.getEmotion(), diary);
        //오늘의 색 저장과 감정들의 type 반환
        Emotion emotion = emotionRepository.save(request.toEmotionEntity(diary));
        //감정 저장
        emotion.toEmotionType(emotionType); //감정 type 저장
        //감정 타입에 따른 코멘트 저장
        diary.toComment(commentService.getComment(emotionType));
        return diary;
    }

    public void delete(long id){
        diaryRepository.deleteById(id);
    }
}
