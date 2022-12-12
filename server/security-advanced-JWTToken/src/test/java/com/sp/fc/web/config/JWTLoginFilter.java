package com.sp.fc.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.fc.user.domain.SpUser;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * packageName : com.sp.fc.web.config
 * fileName : JWTLoginFilter
 * author : taeil
 * date : 2022/12/12
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/12        taeil                   최초생성
 */

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
    // 아이디 비밀번호를 줘서 유효한 사용자면 인증토큰을 내려줌

    private ObjectMapper objectMapper = new ObjectMapper();


    public JWTLoginFilter (AuthenticationManager authenticationManager) {
        super(authenticationManager);
        // 어떤 url로 들어올때 필터로 로그인을 처리할것인가를 명시 (POST URL)
        setFilterProcessesUrl("/login");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        UserLoginForm userLogin = objectMapper.readValue(request.getInputStream(), UserLoginForm.class);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userLogin.getUsername(), userLogin.getPassword(), null
        );

        //userDetails
        return getAuthenticationManager().authenticate(token);
    }

    // user에 대한 검증이 성공을 했을경우
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException
    {
        // 인증결과에서 principal을 담고 ..
        SpUser user = (SpUser) authResult.getPrincipal();
        // 토큰 만들기
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+JWTUtil.makeAuthToken(user));

        // 헤더에 제이슨값을 받을수있게 설정함
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        response.getOutputStream().write(objectMapper.writeValueAsBytes(user));
    }
}