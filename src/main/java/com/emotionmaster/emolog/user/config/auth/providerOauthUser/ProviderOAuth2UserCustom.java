package com.emotionmaster.emolog.user.config.auth.providerOauthUser;


public interface ProviderOAuth2UserCustom {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}

