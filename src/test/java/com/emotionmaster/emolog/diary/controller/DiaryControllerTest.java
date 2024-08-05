package com.emotionmaster.emolog.diary.controller;

import com.emotionmaster.emolog.color.domain.Color;
import com.emotionmaster.emolog.color.repository.ColorRepository;
import com.emotionmaster.emolog.comment.domain.Comment;
import com.emotionmaster.emolog.comment.repository.CommentRepository;
import com.emotionmaster.emolog.config.jwt.TokenProvider;
import com.emotionmaster.emolog.diary.domain.Diary;
import com.emotionmaster.emolog.diary.dto.request.AddDiaryRequest;
import com.emotionmaster.emolog.diary.dto.request.Qa;
import com.emotionmaster.emolog.diary.repository.DiaryRepository;
import com.emotionmaster.emolog.emotion.domain.Emotion;
import com.emotionmaster.emolog.emotion.domain.EmotionType;
import com.emotionmaster.emolog.emotion.repository.EmotionRepository;
import com.emotionmaster.emolog.q_a.domain.Q_A;
import com.emotionmaster.emolog.q_a.repository.QaRepository;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest //test용 application context
@AutoConfigureMockMvc //mockmvc 생성 및 자동 구성
class DiaryControllerTest {
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
    EmotionRepository emotionRepository;

    @Autowired
    QaRepository qaRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenProvider tokenProvider;

    User user;

    @BeforeEach
    public void setMockMvc(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
//        diaryRepository.deleteAll();
    }


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

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }

    @DisplayName("saveDiary() : 전달받은 일기와 Q&A, 감정까지 모두 저장한다.")
    @Test
//    @BeforeEach
    public void saveDiary() throws Exception{
        //given
        final String url ="/api/diary";
        final LocalDate now = now();
        final String content = "content";
        final String emotion = "가벼운,가뿐한,희망찬,희열";
        final String question = "questions";
        final String answer = "answers";
        final AddDiaryRequest request =
                new AddDiaryRequest(now, new Qa(question, answer), emotion, content, "url");

        final String requestBody = objectMapper.writeValueAsString(request);
        String token = tokenProvider.generateToken(user, Duration.ofHours(4));
        //when
        ResultActions result = mockMvc.perform(post(url)
                        .header("Authorization", "Bearer "+token )
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        //CREATED 인지, emotion, Q_A가 잘 들어갔는지
        result.andExpect(status().isCreated());

        List<Diary> diary = diaryRepository.findAll();
        List<Emotion> emotions = emotionRepository.findAll();
        List<Q_A> q_a = qaRepository.findAll();
        List<Color> colors = colorRepository.findAll();

        assertThat(colors.size()).isEqualTo(1);

        //내용 확인
        assertThat(diary.get(0).getDate()).isEqualTo(now);
        assertThat(diary.get(0).getContent()).isEqualTo(content);

        assertThat(emotions.get(0).getEmotion()).isEqualTo(emotion);
        assertThat(emotions.get(0).getEmotionType()).isEqualTo(EmotionType.POS_UNACT);

        assertThat(q_a.get(0).getQuestion()).isEqualTo(question);
        assertThat(q_a.get(0).getAnswer()).isEqualTo(answer);

        //join 확인
        assertThat(emotions.get(0).getDiary().getId()).isEqualTo(diary.get(0).getId());
        assertThat(q_a.get(0).getDiary().getId()).isEqualTo(diary.get(0).getId());

        //코멘트 추가
        Comment comment = diary.get(0).getComment();

        assertThat(comment.getType()).isEqualTo(EmotionType.POS_UNACT);
        assertThat(comment.getComment()).isEqualTo(commentRepository.findById(comment.getId()).get().getComment());

        assertThat(diary.get(0).getUser()).isNotNull();
    }

    @DisplayName("deleteDiary() : id 값에 맞춰 일기를 삭제한다")
    @Test
    public void deleteDiary() throws Exception {
        final String url = "/api/diary/{id}";
        Diary diary = diaryRepository.save(
                Diary.builder()
                        .date(LocalDate.now())
                        .content("content")
                        .build());

        ResultActions results = mockMvc.perform(delete(url,diary.getId()));

        List<Diary> diaryList = diaryRepository.findAll();

        results.andExpect(status().isOk());

        assertThat(diaryList.size()).isZero();
    }

    @DisplayName("getSummary() : id 값에 맞춰 일기 요약본을 가져온다.")
    @Test
    public void getSummary() throws Exception{
        final String url = "/api/diary/summary/{id}";
        final long id = 1;

        ResultActions result = mockMvc.perform(get(url, id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        System.out.println(result.andReturn().getResponse().getContentAsString());

        result.andExpect(jsonPath("$.color").isNotEmpty())
                .andExpect(jsonPath("$.diary").isNotEmpty())
                .andExpect(jsonPath("$.emotion").isNotEmpty())
                .andExpect(jsonPath("$.comment").isNotEmpty());
    }

    @DisplayName("getDiary() : id 값에 맞춰 일기를 가져온다.")
    @Test
    public void getDiary() throws Exception{
        final String url = "/api/diary/{id}";
        final long id = 1;

        ResultActions result = mockMvc.perform(get(url, id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        System.out.println(result.andReturn().getResponse().getContentAsString());

        result.andExpect(jsonPath("$.color").isNotEmpty())
                .andExpect(jsonPath("$.diary").isNotEmpty())
                .andExpect(jsonPath("$.emotion").isNotEmpty())
                .andExpect(jsonPath("$.comment").isNotEmpty())
                .andExpect(jsonPath("$.q_a").isNotEmpty());
    }
}