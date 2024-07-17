package com.emotionmaster.emolog.user.config.auth.providerOauthUser;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class ProviderOAuth2UserNaver implements ProviderOAuth2UserCustom {
    private Map<String, Object> attributes;

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
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
