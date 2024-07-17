package com.emotionmaster.emolog.user.config.jwt;

import com.emotionmaster.emolog.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
//Token valididy 검사
public class TokenProvider {

    private final JwtProperties jwtProperties;

    //Token 생성
    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    //Token 생성 메소드
    // 이메일, 아이디
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    //토큰 유효성 검사
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    //토큰 secret_key  복호화
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }


    //Spring Security - 토큰 기반으로 인증 정보 가져오는 메소드
    public Authentication getAuthentication(String token) {
        //properties 에 저장한 secretkey 로 토큰 복호화 후 Claim(id) 반환
        Claims claims = getClaims(token);
        //토큰의 sub에서 사용자의 Id 추출 및 사용자가 가지는 권한 정보 생성
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        //복호화된 사용자 Id 를 기반으로 Spring Security의 UserDetails 객체 생성
        // 이를 이용해 사용자 인증정보, 권한 정보 나타내는 터ㅗ큰 생성
        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject
                (), "", authorities), token, authorities);
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }


}
