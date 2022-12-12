package com.sp.fc.web.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.service.SpUserService;
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
    private SpUserService userService;

    public JWTLoginFilter (AuthenticationManager authenticationManager, SpUserService userService) {
        super(authenticationManager);
        this.userService = userService;
        // 어떤 url로 들어올때 필터로 로그인을 처리할것인가를 명시 (POST URL)
        setFilterProcessesUrl("/login");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        // 개발자가 원하는 폼으로 안올수도 있으므로 폼에대한 예외처리도 필요함
        UserLoginForm userLogin = objectMapper.readValue(request.getInputStream(), UserLoginForm.class);

        // refresh 토큰을 조사함
        if (userLogin.getRefreshToken() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userLogin.getUsername(), userLogin.getPassword(), null
            );
            return getAuthenticationManager().authenticate(token);
        } else {
            // refresh 토큰이 있다면,
            VerifyResult verify = JWTUtil.verify(userLogin.getRefreshToken());
            // 유효한 토큰이라면,
            if (verify.isSuccess()) {
                // 유효한 토큰이면 Authentication에게 위임하지 않고 직접 토큰을 발급(연장)
                SpUser user = (SpUser) userService.loadUserByUsername(verify.getUsername());
               return new UsernamePasswordAuthenticationToken(
                        user, user.getAuthorities()
                );
            } else {
                throw new TokenExpiredException("refresh token expired");
            }
        }
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
        response.setHeader("auth_token", "Bearer "+JWTUtil.makeAuthToken(user));
        response.setHeader("refresh_token", "Bearer "+JWTUtil.makeRefreshToken(user));
        // 헤더에 제이슨값을 받을수있게 설정함
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        response.getOutputStream().write(objectMapper.writeValueAsBytes(user));
    }
}
