package com.emotionmaster.emolog.config;


import com.emotionmaster.emolog.config.auth.OAuth2SuccessHandler;
import com.emotionmaster.emolog.config.auth.OAuth2UserCustomService;
import com.emotionmaster.emolog.config.auth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.emotionmaster.emolog.config.jwt.TokenProvider;
import com.emotionmaster.emolog.user.repository.RefreshTokenRepository;
import com.emotionmaster.emolog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
//@bean 정의를 통해 의존성 주입 용이하도록
@Configuration
//OAuth2 + JWt 같이 사용하는 스프링 시큐리티설정 클래스! (기존 스프링시큐리티-세션,폼 => OAuth 쿠키,토큰
//
public class WebOAuthSecurityConfig {
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    //h2, img css js 에 대한 스프링 시큐리티 비활성화
    //나중에 추가 설정 mysql 이미지 등등
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/img/**", "/css/**", "/js/**");
    }


    //Filter Chain 구성 - HttpSecurity 사용해 보안설정 지정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //CSRF보호, HTTP기본인증, Form, Logout 비활성화
        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);

        //세션 비활성화 및 앱 상대 비지정
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        //모든 요청이 처리되기전 토근 인증 수행
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        //특정 URL 패턴에 대한 접근권한 설정
        //토큰 -> 인증없이
        ///api 경로는 인증된 사용자만
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/token").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
        );

        //OAuth2 로그인 구성
        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                //인증요청 쿠키에 기반한 저장소에 저장
                .authorizationEndpoint(authorization -> authorization
                        .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                )
                //로그인 성공 핸들러
                .successHandler(oAuth2SuccessHandler)
                //사용자 정보 가져우기
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(oAuth2UserCustomService)
                )
        );
        http.logout(logout -> logout
                .logoutSuccessUrl("/login")
        );

        //예외처리
        //인증 X -> 401 UnAuthorized Status Code 반환
        http.exceptionHandling(exceptions -> exceptions
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**"))
        );

        return http.build();
    }


    //보조 Bean 메소드

    //토큰기반 인증 필터
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    //OAuth2 인증 요청 쿠키에 저장 및 관리
    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }


    //비밀번호 해실
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



















