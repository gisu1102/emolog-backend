package com.emotionmaster.emolog.color.controller;

import com.emotionmaster.emolog.color.dto.response.ColorAndDate;
import com.emotionmaster.emolog.color.service.ColorService;
import com.emotionmaster.emolog.diary.domain.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/color")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;

    @GetMapping
    public ResponseEntity<List<ColorAndDate>> findColorByMonth(
            @RequestParam(value = "month") int month,
            @RequestParam(value = "week", required = false, defaultValue = "0") int week) {
        List<ColorAndDate> colorList;
        if (week == 0) {
            colorList = colorService.findAllColorOfMonth(month);
        } else {
            colorList = colorService.findAllColorOfWeek(month, week);
        }

        return ResponseEntity.ok().body(colorList);
    }
}
