package com.emotionmaster.emolog.config.auth.providerOauthUser;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class ProviderOAuth2UserKakao implements ProviderOAuth2UserCustom {
    private Map<String, Object> attributes;

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

