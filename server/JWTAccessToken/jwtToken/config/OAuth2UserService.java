package com.trading.day.config.jwtConfig;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * packageName : com.trading.day.config.jwtConfig
 * fileName : OAuth2UserService
 * author : taeil
 * date : 2023/01/08
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2023/01/08        taeil                   최초생성
 */
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
    String clientId = "";
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        clientId = userRequest.getClientRegistration().getRegistrationId();

        return super.loadUser(userRequest);
    }

    public String getClientId() {
        return clientId;
    }


}