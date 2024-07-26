package com.emotionmaster.emolog.config.auth.providerOauthUser;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;


@NoArgsConstructor
@Getter
@Component
public class ProviderOAuth2UserGoogle implements ProviderOAuth2UserCustom {

    private Map<String, Object> attributes;

    // 새 생성자 추가
    public ProviderOAuth2UserGoogle(Map<String, Object> attributes) {

        this.attributes = attributes;
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
