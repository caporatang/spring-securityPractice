package com.sp.fc.web.config;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * packageName : com.sp.fc.web.config
 * fileName : SpOAuth2UserService
 * author : taeil
 * date : 2023/01/03
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2023/01/03        taeil                   최초생성
 */
@Component
public class SpOAuth2UserService extends DefaultOAuth2UserService {
    // 카카오 네이버 페이스북
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 유저 정보가 우리 데이터베이스에 없다면.. 우리쪽 사용자로 등록을 한다거나 하는 로직을 추가
        return super.loadUser(userRequest);
    }
}