package com.emotionmaster.emolog.config.auth;

import com.emotionmaster.emolog.config.auth.providerOauthUser.ProviderOAuth2UserCustom;
import com.emotionmaster.emolog.config.auth.providerOauthUser.ProviderOAuth2UserGoogle;
import com.emotionmaster.emolog.config.auth.providerOauthUser.ProviderOAuth2UserKakao;
import com.emotionmaster.emolog.config.auth.providerOauthUser.ProviderOAuth2UserNaver;
import com.emotionmaster.emolog.user.domain.User;
import com.emotionmaster.emolog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


//OAuth 에서 제공하는 정보 기반으로 유저 객체 관리해주는 메소드 loadUser 활용
@RequiredArgsConstructor
@Service
@Slf4j
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    //사용자 정보 받아오고(loadUser) - 정보 저장(saveOrUpdate)
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        String accessToken = userRequest.getAccessToken().getTokenValue();
        log.info("access token : " + accessToken);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String providedId = userRequest.getClientRegistration().getRegistrationId();
       //정상
        log.info(providedId);
        ProviderOAuth2UserCustom oAuth2UserInfo = null;
        if ("google".equals(providedId)) {
            log.info("LoadGoogleUserInfo");
            oAuth2UserInfo = new ProviderOAuth2UserGoogle(oAuth2User.getAttributes());
        } else if ("kakao".equals(providedId)) {
            oAuth2UserInfo = new ProviderOAuth2UserKakao(oAuth2User.getAttributes());
        } else if ("naver".equals(providedId)) {
            oAuth2UserInfo = new ProviderOAuth2UserNaver(oAuth2User.getAttributes());
        }

        if (oAuth2UserInfo != null) { // null 체크 후 사용
            log.info("oAuth2UserInfo" + oAuth2UserInfo);
            saveOrUpdate(oAuth2UserInfo);
        }
        return oAuth2User;
    }


    //각 플랫폼 별 추출한 정보 db저장
    private User saveOrUpdate(ProviderOAuth2UserCustom providerOAuth2UserCusotom) {
        String providerId = providerOAuth2UserCusotom.getProviderId();

        String provider = providerOAuth2UserCusotom.getProvider();
        String email = providerOAuth2UserCusotom.getEmail();
        String name = providerOAuth2UserCusotom.getName();

        log.info("UserInfo" + provider + email + name);
        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(User.builder()
                        .email(email)
                        .name(name)
                        .oauthType(provider)
                        .build());
        return userRepository.save(user);
    }


















}
