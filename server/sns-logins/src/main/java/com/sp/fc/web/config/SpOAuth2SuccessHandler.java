package com.sp.fc.web.config;

import com.sp.fc.user.domain.SpOAuth2User;
import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.service.SpUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * packageName : com.sp.fc.web.config
 * fileName : SpOAuth2SuccessHandler
 * author : taeil
 * date : 2023/01/03
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2023/01/03        taeil                   최초생성
 */
@Component
public class SpOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private SpUserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException
    {
            Object principal = authentication.getPrincipal();
            // oauth2가 상위 oidcUser가 하위객체 , 하위객체인지 먼저 체크
            if(principal instanceof OidcUser) {
                //google사용자
                SpOAuth2User oauth = SpOAuth2User.Provider.google.convert((OidcUser) principal);
                SpUser user = userService.load(oauth);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities())
                );

            } else if(principal instanceof OAuth2User) {
                //naver, kakao..등
                SpOAuth2User oauth = SpOAuth2User.Provider.naver.convert((OAuth2User) principal);
                SpUser user = userService.load(oauth);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities())
                );
            }

            // 리다이렉트
            request.getRequestDispatcher("/").forward(request, response);
    }
}