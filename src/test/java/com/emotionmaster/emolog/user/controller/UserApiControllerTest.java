package com.emotionmaster.emolog.user.controller;

import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.dto.request.UserRequestDto;
import com.emotionmaster.emolog.user.dto.response.UserInfoResponseDto;
import com.emotionmaster.emolog.user.dto.response.UserResponseDto;
import com.emotionmaster.emolog.user.repository.UserRepository;
import com.emotionmaster.emolog.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll();

        user = userRepository.save(User.builder()
                .email("user@example.com")
                .password("password")
                .nickname("nickname")
                .age(30)
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }

    @Test
    @DisplayName("로그아웃: 로그아웃을 수행하고 결과를 반환한다.")
    public void logout() throws Exception {
        // given
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", "Logout successful");

        when(userService.logout(Mockito.any(HttpServletResponse.class)))
                .thenReturn(responseMap);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/logout")
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
                .andExpect(status().isOk()) //
                .andExpect(status().isOk()) // 상태 코드 확인
                .andExpect(content().json(objectMapper.writeValueAsString(responseMap))); // 응답 내용 확인
    }


    @DisplayName("회원 수정: 유저 정보를 업데이트한다.")
    @Test
    public void updateUser() throws Exception {
        // given
        final String url = "/api/updateUser/" + user.getId(); // 경로 수정
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setNickname("newNickname");
        requestDto.setAge(35);

        final String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions resultActions = mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isOk()) // 상태 코드 수정
                .andExpect(jsonPath("$.nickname").value("newNickname"))
                .andExpect(jsonPath("$.age").value(35));
    }
    @DisplayName("회원 조회: 유저 정보를 조회한다.")
    @Test
    public void getUserInfo() throws Exception {
        // given
        final String url = "/api/userInfo/" + user.getId();

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
                .andExpect(status().isOk())
                 .andExpect(jsonPath("$.nickname").value("nickname"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @DisplayName("회원 탈퇴: 유저를 삭제한다.")
    @Test
    public void deleteUser() throws Exception {
        // given
        final String url = "/api/userDelete/" + user.getId();

        // when
        ResultActions resultActions = mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }
}
