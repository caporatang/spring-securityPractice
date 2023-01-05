package com.trading.day.config.jwtConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.day.member.domain.Member;
import com.trading.day.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * packageName : com.trading.day.config.jwtConfig
 * fileName : OAuth2SuccessHandler
 * author : taeil
 * date : 2023/01/03
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2023/01/03        taeil                   최초생성
 */
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private MemberService memberService;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User principal = (OAuth2User) authentication.getPrincipal();


//        String email = principal.getAttribute("email");
//        Member findMember = memberService.socialFindMember(email);
//
//        if(ObjectUtils.isEmpty(findMember)) {
//            response.setHeader("socialLogin", email);
//            response.sendRedirect("http://localhost:3000/member/signup");
//        }

//        if(principal instanceof OidcUser) {
//            //google
//
//        } else if(principal instanceof OAuth2User) {
//            //naver
//
//        }






    }
}