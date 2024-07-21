package com.emotionmaster.emolog.color.controller;

import com.emotionmaster.emolog.color.domain.Color;
import com.emotionmaster.emolog.color.repository.ColorRepository;
import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.diary.repository.DiaryRepository;
import com.emotionmaster.emolog.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest //test용 application context
@AutoConfigureMockMvc //mockmvc 생성 및 자동 구성
class ColorControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    DiaryRepository diaryRepository;

    @Autowired
    ColorRepository colorRepository;

    @BeforeEach
    public void setMockMvc(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        diaryRepository.deleteAll();
    }

    @Test
    @DisplayName("findAllColorOfMonth() : month 값에 맞춰 월간 색을 모두 조회한다")
    public void findAllColorOfMonth() throws Exception {
        final String url = "/api/color";

        final String content1 = "content1";
        final String content7 = "content7";

        final String color1 = "FFFFFF";
        final String color7 = "000000";

        final LocalDate date1 = LocalDate.of(2024, 1, 20);
        final LocalDate date7 = LocalDate.of(2024, 7, 20);

        Diary diaryOf7 = diaryRepository.save(new Diary(date7, content7,
                DateUtil.getWeekOfMonthByDate(date7), date7.getDayOfWeek()));
        colorRepository.save(new Color(0, 0, 0, color7, diaryOf7));

        Diary diaryOf1 = diaryRepository.save(new Diary(date1, content1,
                DateUtil.getWeekOfMonthByDate(date1), date1.getDayOfWeek()));
        colorRepository.save(new Color(0, 0, 0, color1, diaryOf1));

        // Test for July (month 7)
        ResultActions resultsOf7 = mockMvc.perform(get(url)
                        .param("month", String.valueOf(7)))
                .andExpect(status().isOk());

        resultsOf7.andExpect(jsonPath("$[0].date").value(String.valueOf(date7)))
                .andExpect(jsonPath("$[0].hexa").value(color7));

        // Test for January (month 1)
        ResultActions resultsOf1 = mockMvc.perform(get(url)
                        .param("month", String.valueOf(1)))
                .andExpect(status().isOk());

        resultsOf1.andExpect(jsonPath("$[0].date").value(String.valueOf(date1)))
                .andExpect(jsonPath("$[0].hexa").value(color1));
    }

    @Test
    @DisplayName("findAllColorOfMonthAndWeek() : week 값에 맞춰 월간 색을 모두 조회한다")
    public void findAllColorOfMonthAndWeek() throws Exception {
        final String url = "/api/color";

        final String content1 = "content1";
        final String content7 = "content7";

        final String color1 = "FFFFFF";
        final String color7 = "000000";

        final LocalDate dateOfWeek4 = LocalDate.of(2024, 7, 21);
        final LocalDate dateOfWeek3 = LocalDate.of(2024, 7, 20);

        Diary diaryOf7 = diaryRepository.save(new Diary(dateOfWeek3, content7,
                DateUtil.getWeekOfMonthByDate(dateOfWeek3), dateOfWeek3.getDayOfWeek()));
        colorRepository.save(new Color(0, 0, 0, color7, diaryOf7));

        Diary diaryOf1 = diaryRepository.save(new Diary(dateOfWeek4, content1,
                DateUtil.getWeekOfMonthByDate(dateOfWeek4), dateOfWeek4.getDayOfWeek()));
        colorRepository.save(new Color(0, 0, 0, color1, diaryOf1));

        // Test for July (month 7)
        ResultActions resultsOf7 = mockMvc.perform(get(url)
                        .param("month", String.valueOf(7))
                        .param("week", String.valueOf(3)))
                .andExpect(status().isOk());

        resultsOf7.andExpect(jsonPath("$[0].date").value(String.valueOf(dateOfWeek3)))
                .andExpect(jsonPath("$[0].hexa").value(color7));

        // Test for January (month 1)
        ResultActions resultsOf1 = mockMvc.perform(get(url)
                        .param("month", String.valueOf(7))
                        .param("week", String.valueOf(4)))
                .andExpect(status().isOk());

        resultsOf1.andExpect(jsonPath("$[0].date").value(String.valueOf(dateOfWeek4)))
                .andExpect(jsonPath("$[0].hexa").value(color1));
    }
}