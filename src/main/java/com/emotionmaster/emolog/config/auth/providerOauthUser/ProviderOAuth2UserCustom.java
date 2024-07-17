package com.emotionmaster.emolog.config.auth.providerOauthUser;


public interface ProviderOAuth2UserCustom {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}

