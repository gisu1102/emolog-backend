package com.emotionmaster.emolog.config.auth.providerOauthUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Component
public class ProviderOAuth2UserGoogle implements ProviderOAuth2UserCustom {

    private Map<String, Object> attributes;

    private final String googleLoginUrl = "https://accounts.google.com";
    private final String googleTokenUrl = "https://oauth2.googleapis.com/token";
    private final String googleUserInfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo";

    @Value("${google.id}")
    private String googleId;
    @Value("${google.redirectUrl}")
    private String googleRedirectUrl;
    @Value("${google.secret}")
    private String googleClientSecret;

    // 새 생성자 추가
    public ProviderOAuth2UserGoogle(Map<String, Object> attributes) {

        this.attributes = attributes;
    }

    public String responseUrl(){
        String googleLoginUrl =
                "https://accounts.google.com/o/oauth2/auth?client_id="
                + googleId + "&redirect_uri=" +
                googleRedirectUrl + "&response_type=code&scope=openid";
        System.out.println("Generated Google Login URL: " + googleLoginUrl);

        return googleLoginUrl;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
