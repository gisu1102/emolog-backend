package com.emotionmaster.emolog.config;

import com.emotionmaster.emolog.config.error.errorcode.UserErrorcode;
import com.emotionmaster.emolog.config.error.exception.UserException;
import com.emotionmaster.emolog.config.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Access Token 이 담긴 Authorization 헤더값 가져운 후 인증 정보 설정
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    //HttpRequest 의 Authorization 헤더 이름
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";



    // 필터링 하는 메소드
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {

        //HTTP 요청에서 Authorization 헤더값을 가져와서 저장 및 토큰으로 추출
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = getAccessToken(authorizationHeader);

        //토큰 유효성 검사
        // 유효 하면 해당 토큰 가져와 설정
        if (tokenProvider.validToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    //Bearer 접두사 삭제 메소드
    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    public String getAccessToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        if (authorizationHeader == null) throw new UserException(UserErrorcode.TOKEN_NOT_FOUND);
        return getAccessToken(authorizationHeader);
    }
}









