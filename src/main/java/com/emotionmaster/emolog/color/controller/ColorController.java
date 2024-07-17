package com.emotionmaster.emolog.color.controller;

import com.emotionmaster.emolog.color.domain.Color;
import com.emotionmaster.emolog.color.dto.request.AddColorRequest;
import com.emotionmaster.emolog.color.service.ColorService;
import com.emotionmaster.emolog.diary.domain.Diary;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/color")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;

    @GetMapping
    public ResponseEntity<List<Map<LocalDate, String>>> findColorByMonth(@RequestParam("month") int month){
        List<Map<LocalDate, String>> colorList = colorService.findAllColorOfMonth(month)
                .stream()
                .map(this::dateAndColor)
                .toList();

        return ResponseEntity.ok()
                .body(colorList);
    }

    private Map<LocalDate, String> dateAndColor(Diary diary){
        return Map.of(diary.getDate(), diary.getColor().getHexa());
    }

    @PostMapping
    public ResponseEntity<Color> saveColor(@RequestBody AddColorRequest request){
        return ResponseEntity.ok()
                .body(colorService.saveColor(request));
    }
}
