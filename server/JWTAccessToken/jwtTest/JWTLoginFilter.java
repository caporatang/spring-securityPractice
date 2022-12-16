package com.trading.day.config.jwtConfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.day.member.domain.Member;
import com.trading.day.member.domain.MemberDTO;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * packageName : com.trading.day.config.jwtConfig
 * fileName : JWTLoginFilter
 * author : taeil
 * date : 2022/12/15
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/15        taeil                   최초생성
 */
@Transactional
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {


    private ObjectMapper objectMapper = new ObjectMapper();

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/login");
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

       Member member = objectMapper.readValue(request.getInputStream(), Member.class);
        System.out.println("@@@@@@JWT LOGIN FILTER" + member.getMemberId());
//        CustomUserDtails customUserDtails = objectMapper.readValue(request.getInputStream(), CustomUserDtails.class);

       UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
               member.getMemberId(), member.getPwd(), null
//               memberDTO.getMemberId(), memberDTO.getPwd(), null
       );

       System.out.println("@@@@@@JWT TOKEN" + token);
       System.out.println("@@@@@@JWT TOKEN" + token.getPrincipal());
        System.out.println("@@@@@@JWT TOKEN" + token.getCredentials());
        // userDetails
        // 매니저에게 토큰 검증해달라고 요청 , 다오 어센티케이터 프로바이더를 통해서 유저디테일즈 서비스에서 검증해줌
        return getAuthenticationManager().authenticate(token);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException
    {

//        CustomUserDtails customUserDtails = (CustomUserDtails) authResult.getPrincipal();

        System.out.println("@@@@@@authResult" + authResult);
        System.out.println("@@@@@@getCredentials" + authResult.getCredentials());
        System.out.println("@@@@@@getPrincipal" + authResult.getPrincipal());
        //Member member = (Member) authResult.getPrincipal();
        UserDetails details = (UserDetails) authResult.getPrincipal();

        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+JWTUtil.makeAuthToknen(details));


        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(details));


    }
}