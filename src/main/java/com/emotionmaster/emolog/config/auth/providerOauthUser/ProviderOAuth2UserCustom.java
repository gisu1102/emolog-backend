package com.emotionmaster.emolog.config.auth.providerOauthUser;


import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public interface ProviderOAuth2UserCustom {

    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();

    static ProviderOAuth2UserCustom create(OAuth2User oAuth2User, String providerId) {
        switch (providerId) {
            case "google":
                return new ProviderOAuth2UserGoogle(oAuth2User.getAttributes());
//            case "kakao":
//                return new ProviderOAuth2UserKakao(oAuth2User.getAttributes());
            case "naver":
                return new ProviderOAuth2UserNaver(oAuth2User.getAttributes());
            default:
                throw new IllegalArgumentException("Unsupported provider: " + providerId);
        }
    }

}

