package com.emotionmaster.emolog.diary.controller;

import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.diary.dto.request.AddDiaryRequest;
import com.emotionmaster.emolog.diary.dto.response.AddDiaryResponse;
import com.emotionmaster.emolog.diary.dto.response.GetDiaryResponse;
import com.emotionmaster.emolog.diary.dto.response.SummaryDiaryResponse;
import com.emotionmaster.emolog.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<AddDiaryResponse> saveDiary(@RequestBody AddDiaryRequest request) throws Exception {
        Diary savedDiary = diaryService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AddDiaryResponse(savedDiary));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(@PathVariable("id") long id){
        diaryService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/summary/{date}")
    public ResponseEntity<SummaryDiaryResponse> getSummary(@PathVariable("date") String date){
        return ResponseEntity.ok()
                .body(diaryService.getSummary(date));
    }

    @GetMapping("/{date}")
    public ResponseEntity<GetDiaryResponse> getDiary(@PathVariable("date") String date){
        return ResponseEntity.ok()
                .body(diaryService.getDiary(date));
    }
}
