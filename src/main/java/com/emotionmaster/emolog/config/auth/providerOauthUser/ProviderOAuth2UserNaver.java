package com.emotionmaster.emolog.config.auth.providerOauthUser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@AllArgsConstructor
@Slf4j
public class ProviderOAuth2UserNaver implements ProviderOAuth2UserCustom {
    private Map<String, Object> attributes;

    @Value("${naver.id}")
    private String naverId;
    @Value("${naver.redirectUrl}")
    private String naverRedirectUrl;
    @Value("${naver.secret}")
    private String kakaoClientSecret;

    // 새 생성자 추가
    public ProviderOAuth2UserNaver(Map<String, Object> attributes) {

        this.attributes = attributes;
    }

    public String responseUrl(){
        String naverLoginUrl =
                "https://nid.naver.com/oauth2.0/authorize?response_type=code" +
                        "&client_id=" + naverId +
                "&redirect_uri="+ naverRedirectUrl +
                        "&state=YOUR_STATE";


        log.info("Generated naver Login URL: " + naverLoginUrl);

        return naverLoginUrl;
    }
    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        Map<String, Object> responseAttributes = (Map<String, Object>) attributes.get("response");
        return (String) responseAttributes.get("email");
    }

    @Override
    public String getName() {
        Map<String, Object> responseAttributes = (Map<String, Object>) attributes.get("response");
        return (String) responseAttributes.get("name");
    }
}
