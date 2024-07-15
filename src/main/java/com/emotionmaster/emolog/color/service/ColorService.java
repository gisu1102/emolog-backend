package com.emotionmaster.emolog.color.service;

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

    public List<Diary> findAllColorOfMonth(int month){
        return diaryRepository.findAllByMonth(month);
    }
}
