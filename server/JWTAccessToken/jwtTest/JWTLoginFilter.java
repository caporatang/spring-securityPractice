package com.trading.day.config.jwtConfig;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.day.member.domain.Member;
import com.trading.day.member.domain.MemberDTO;
import com.trading.day.member.service.MemberService;
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
    private MemberService memberService;
    public JWTLoginFilter(AuthenticationManager authenticationManager, MemberService memberService) {
        super(authenticationManager);
        this.memberService = memberService;
        setFilterProcessesUrl("/login");
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        // --> 로그인 폼에 대한 예외처리도 추가..해야함...
        Member member = objectMapper.readValue(request.getInputStream(), Member.class);

        if(member.getRefreshToken() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    member.getMemberId(), member.getPwd(), null
            );
            // 매니저에게 토큰 검증해달라고 요청 , 다오 어센티케이터 프로바이더를 통해서 유저디테일즈 서비스에서 검증해줌
            return getAuthenticationManager().authenticate(token);
        } else {
            //refreshToken이 왔다면
            VerifyResult verify = JWTUtil.verify(member.getRefreshToken());
            if(verify.isSuccess()) {
                //AuthenticationManager에게 위임하지 않고, 바로 통행증을 만들어서 보내줌
                UserDetails details = memberService.loadUserByUsername(verify.getMemberId());
                return new UsernamePasswordAuthenticationToken(
                        details, details.getAuthorities()
                );
            } else {
                throw new TokenExpiredException("refresh token expired");
            }
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException
    {
        UserDetails details = (UserDetails) authResult.getPrincipal();

        // Bearer은 꼭 리스폰스에 담아서 갈 필요는 없음 --> 서버에서 필요한것
        response.setHeader("auth_token", JWTUtil.makeAuthToknen(details));
        response.setHeader("refresh_token", JWTUtil.makeRefreshToken(details));

        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(details));


    }
}