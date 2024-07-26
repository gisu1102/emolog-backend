package com.emotionmaster.emolog.config.auth.providerOauthUser;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@NoArgsConstructor
@Component
@Slf4j
public class ProviderOAuth2UserNaver implements ProviderOAuth2UserCustom {
    private Map<String, Object> attributes;


    // 새 생성자 추가
    public ProviderOAuth2UserNaver(Map<String, Object> attributes) {

        this.attributes = attributes;
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
