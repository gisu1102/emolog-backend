package com.emotionmaster.emolog.color.service;

import com.emotionmaster.emolog.color.domain.Color;
import com.emotionmaster.emolog.color.dto.request.AddColorRequest;
import com.emotionmaster.emolog.color.repository.ColorRepository;
import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.diary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorService {

    private final DiaryRepository diaryRepository;
    private final ColorRepository colorRepository;

    public Color saveColor(AddColorRequest request){
        /*
        todo DB에서 감정들에 대한 rgb 값을 가져와서 계산하고 그 값을 저장
         */
        return null;
    }

    public List<Diary> findAllColorOfMonth(int month){
        return diaryRepository.findAllByMonth(month);
    }
}
