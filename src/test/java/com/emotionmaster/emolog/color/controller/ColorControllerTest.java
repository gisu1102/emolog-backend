package com.emotionmaster.emolog.color.controller;

import com.emotionmaster.emolog.color.domain.Color;
import com.emotionmaster.emolog.color.repository.ColorRepository;
import com.emotionmaster.emolog.config.jwt.TokenProvider;
import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.diary.repository.DiaryRepository;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.repository.UserRepository;
import com.emotionmaster.emolog.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
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

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    public void setMockMvc(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
//        diaryRepository.deleteAll();
    }

    String token;
    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .nickname("nickname")
                .age(25)
                .oauthType("google")
                .build());

        token = tokenProvider.generateToken(user, Duration.ofHours(4));

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
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
                DateUtil.getWeekOfMonthByDate(date7), date7.getDayOfWeek(), user));
        colorRepository.save(new Color(0, 0, 0, color7, diaryOf7));

        Diary diaryOf1 = diaryRepository.save(new Diary(date1, content1,
                DateUtil.getWeekOfMonthByDate(date1), date1.getDayOfWeek(), user));
        colorRepository.save(new Color(0, 0, 0, color1, diaryOf1));

        // Test for July (month 7)
        ResultActions resultsOf7 = mockMvc.perform(get(url)
                        .header("Authorization", "Bearer "+token)
                        .param("month", String.valueOf(7)))
                .andExpect(status().isOk());

        resultsOf7.andExpect(jsonPath("$[0].date").value(String.valueOf(date7)))
                .andExpect(jsonPath("$[0].hexa").value(color7));

        // Test for January (month 1)
        ResultActions resultsOf1 = mockMvc.perform(get(url)
                        .header("Authorization", "Bearer "+token)
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

        final LocalDate dateOfWeek4 = LocalDate.of(2024, 7, 22);
        final LocalDate dateOfWeek3 = LocalDate.of(2024, 7, 20);

        Diary diaryOf7 = diaryRepository.save(new Diary(dateOfWeek3, content7,
                DateUtil.getWeekOfMonthByDate(dateOfWeek3), dateOfWeek3.getDayOfWeek(), user));
        colorRepository.save(new Color(0, 0, 0, color7, diaryOf7));

        Diary diaryOf1 = diaryRepository.save(new Diary(dateOfWeek4, content1,
                DateUtil.getWeekOfMonthByDate(dateOfWeek4), dateOfWeek4.getDayOfWeek(), user));
        colorRepository.save(new Color(0, 0, 0, color1, diaryOf1));

        // Test for July (month 7)
        ResultActions resultsOf7 = mockMvc.perform(get(url)
                        .header("Authorization", "Bearer "+token)
                        .param("month", String.valueOf(7))
                        .param("week", String.valueOf(3)))
                .andExpect(status().isOk());

        System.out.println(resultsOf7.andReturn().getResponse().getContentAsString());


        resultsOf7.andExpect(jsonPath("$[0].date").value(String.valueOf(dateOfWeek3)))
                .andExpect(jsonPath("$[0].hexa").value(color7));

        // Test for January (month 1)
        ResultActions resultsOf1 = mockMvc.perform(get(url)
                        .header("Authorization", "Bearer "+token)
                        .param("month", String.valueOf(7))
                        .param("week", String.valueOf(4)))
                .andExpect(status().isOk());

        System.out.println(resultsOf1.andReturn().getResponse().getContentAsString());

        resultsOf1.andExpect(jsonPath("$[0].date").value(String.valueOf(dateOfWeek4)))
                .andExpect(jsonPath("$[0].hexa").value(color1));
    }

    @Test
    @DisplayName("getColorByUserId() : 유저에 해당하는 색 반환")
    public void getColorByUserId() throws Exception{
        final String url = "/api/color";

        User user2 = userRepository.save(User.builder()
                .email("test2@naver.com")
                .build());

        Diary diary1 = diaryRepository.save(Diary.builder()
                .date(LocalDate.now())
                .content("user1")
                .user(user)
                .build()
        );
        colorRepository.save(new Color(0, 0, 0, "FFFFFF", diary1));


        Diary diary2 = diaryRepository.save(Diary.builder()
                .date(LocalDate.now())
                .content("user2")
                .user(user2)
                .build()
        );
        colorRepository.save(new Color(0, 0, 0, "000000", diary2));


        ResultActions result = mockMvc.perform(get(url)
                .header("Authorization", "Bearer "+token)
                .param("month", String.valueOf(7)))
                .andExpect(status().isOk());

        result.andExpect(jsonPath("$[0].hexa").value("FFFFFF"));
    }

}