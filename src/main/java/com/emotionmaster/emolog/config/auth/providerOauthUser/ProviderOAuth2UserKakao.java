package com.emotionmaster.emolog.config.auth.providerOauthUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Component
@Slf4j
public class ProviderOAuth2UserKakao implements ProviderOAuth2UserCustom {
    private Map<String, Object> attributes;

    @Value("${kakao.id}")
    private String kakaoId;
    @Value("${kakao.redirectUrl}")
    private String kakaoRedirectUrl;
    @Value("${kakao.secret}")
    private String kakaoClientSecret;

    // 새 생성자 추가
    public ProviderOAuth2UserKakao(Map<String, Object> attributes) {

        this.attributes = attributes;
    }

    public String responseUrl(){
        String kakaoLoginUrl =
                "https://kaouth.kakao.com/oauth2/authorize?client_id="
                        + kakaoId + "&redirect_uri=" +
                        kakaoRedirectUrl + "&response_type=code";
        log.info("Generated Kakao Login URL: " + kakaoLoginUrl);

        return kakaoLoginUrl;
    }


    @Override
    public String getProviderId() {
        // Long 타입
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) ((Map) attributes.get("kakao_account")).get("email");
    }

    @Override
    public String getName() {
        return (String) ((Map) attributes.get("properties")).get("nickname");
    }
}

