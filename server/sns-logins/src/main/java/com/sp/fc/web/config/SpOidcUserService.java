package com.sp.fc.web.config;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

/**
 * packageName : com.sp.fc.web.config
 * fileName : SpOidcUserService
 * author : taeil
 * date : 2023/01/03
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2023/01/03        taeil                   최초생성
 */
@Component
public class SpOidcUserService extends OidcUserService {
    // google은 Oidc 스펙을 따르기 때문에 구글은 따로 구성
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // 유저 정보가 우리 데이터베이스에 없다면.. 우리쪽 사용자로 등록을 한다거나 하는 로직을 추가
        return super.loadUser(userRequest);
    }
}