package com.emotionmaster.emolog.diary.service;

import com.emotionmaster.emolog.color.service.ColorService;
import com.emotionmaster.emolog.comment.service.CommentService;
import com.emotionmaster.emolog.config.error.errorcode.DiaryErrorCode;
import com.emotionmaster.emolog.config.error.exception.DiaryException;
import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.diary.dto.request.AddDiaryRequest;
import com.emotionmaster.emolog.diary.dto.response.GetDiaryResponse;
import com.emotionmaster.emolog.diary.dto.response.SummaryDiaryResponse;
import com.emotionmaster.emolog.diary.repository.DiaryRepository;
import com.emotionmaster.emolog.emotion.domain.Emotion;
import com.emotionmaster.emolog.emotion.domain.EmotionType;
import com.emotionmaster.emolog.emotion.repository.EmotionRepository;
import com.emotionmaster.emolog.q_a.repository.QaRepository;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final QaRepository qaRepository;
    private final EmotionRepository emotionRepository;
    private final DiaryRepository diaryRepository;

    private final ColorService colorService;
    private final CommentService commentService;
    private final UserService userService;

    @Transactional
    public Diary save(AddDiaryRequest request){
        if (diaryRepository.findByDate(request.getDate()).isPresent())
            throw new DiaryException(DiaryErrorCode.DIARY_DUPLICATED);
        User user = userService.getCurrentUser();

        Diary diary = diaryRepository.save(request.toDiaryEntity(user));
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
        isAuthorized(id);
        diaryRepository.deleteById(id);
    }

    public SummaryDiaryResponse getSummary(long id) {
        Diary diary = isAuthorized(id);
        SummaryDiaryResponse response = new SummaryDiaryResponse();
        response.toSummary(diary);
        return response;
    }

    public GetDiaryResponse getDiary(long id){
        Diary diary = isAuthorized(id);
        GetDiaryResponse response = new GetDiaryResponse();
        response.toSummary(diary);
        return response;
    }

    private Diary isAuthorized(long id){
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new DiaryException(DiaryErrorCode.DIARY_NOT_FOUND));
        if (diary.getUser().equals(userService.getCurrentUser())){
            return diary;
        } else {
            throw new DiaryException(DiaryErrorCode.DIARY_UNAUTHORIZED);
        }
    }
}
