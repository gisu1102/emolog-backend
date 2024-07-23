package com.emotionmaster.emolog.user.controller;

import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.dto.request.UserRequestDto;
import com.emotionmaster.emolog.user.dto.response.UserInfoResponseDto;
import com.emotionmaster.emolog.user.dto.response.UserResponseDto;
import com.emotionmaster.emolog.user.repository.UserRepository;
import com.emotionmaster.emolog.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UserApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;


    User user;

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

    @DisplayName("updateUser: 사용자 정보 업데이트에 성공한다.")
    @Test
    public void updateUser() throws Exception {
        // given
        final String url = "/api/updateUser/{id}";
        final Long userId = user.getId();
        final String newEmail = "newemail@gmail.com";
        final String newNickname = "newnickname";
        final int newAge = 30;

        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setEmail(newEmail);
        requestDto.setNickname(newNickname);
        requestDto.setAge(newAge);

        final String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions result = mockMvc.perform(put(url, userId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(newNickname))
                .andExpect(jsonPath("$.age").value(newAge));
    }

    @DisplayName("userInfo: 사용자 정보 조회에 성공한다.")
    @Test
    public void userInfo() throws Exception {
        // given
        final String url = "/api/userInfo/{id}";
        final Long userId = user.getId();

        // when
        ResultActions result = mockMvc.perform(get(url, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(user.getNickname()))
                .andExpect(jsonPath("$.age").value(user.getAge()));
    }

    @DisplayName("userDelete: 사용자 삭제에 성공한다.")
    @Test
    public void userDelete() throws Exception {
        // given
        final String url = "/api/userDelete/{id}";
        final Long userId = user.getId();

        // when
        mockMvc.perform(delete(url, userId))
                .andExpect(status().isNoContent());

        // then
        assertThat(userRepository.findById(userId)).isEmpty();
    }

    @DisplayName("logout: 구글 로그아웃에 성공한다.")
    @Test
    public void logout() throws Exception {
        // given
        final String url = "/logout/{id}";
        final Long userId = user.getId();
        final String accessToken = "dummyAccessToken"; // Mocked access token

        // when
        ResultActions result = mockMvc.perform(post(url, userId)
                .header("Authorization", "Bearer " + accessToken)
                .cookie(new Cookie("access_token", "dummyToken")));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"));

        MockHttpServletResponse response = result.andReturn().getResponse();
        Cookie[] cookies = response.getCookies();

        assertThat(cookies).isNotNull();
        boolean cookieCleared = false;

        for (Cookie cookie : cookies) {
            if ("access_token".equals(cookie.getName())) {
                //쿠키값 비어있는지 확인
                assertThat(cookie.getValue()).isEmpty();
                //만료시간 =0
                assertThat(cookie.getMaxAge()).isEqualTo(0);
                //사이트의 모든 경로에서 유효한지
                assertThat(cookie.getPath()).isEqualTo("/");
                assertThat(cookie.isHttpOnly()).isTrue();
                //https 에서만
                assertThat(cookie.getSecure()).isTrue();
                cookieCleared = true;
            }
        }

        assertThat(cookieCleared).isTrue();
    }

}
