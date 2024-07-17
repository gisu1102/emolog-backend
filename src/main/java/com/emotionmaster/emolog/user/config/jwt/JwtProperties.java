package com.emotionmaster.emolog.user.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {

    //application.yml 의 jwt.issuer 값, secret key 에는 jwt.secret_key
    private String issuer;
    private String secretKey;
}
