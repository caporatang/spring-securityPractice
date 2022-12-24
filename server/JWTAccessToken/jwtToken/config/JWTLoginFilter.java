package com.trading.day.config.jwtConfig;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.day.jwtToken.domain.ResponseTokenDTO;
import com.trading.day.jwtToken.domain.TokenDTO;
import com.trading.day.jwtToken.domain.TokenManage;
import com.trading.day.jwtToken.service.TokenService;
import com.trading.day.member.domain.Member;
import com.trading.day.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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
    private TokenService tokenService;
    public JWTLoginFilter(AuthenticationManager authenticationManager,
                          MemberService memberService, TokenService tokenService)
    {
        super(authenticationManager);
        this.memberService = memberService;
        this.tokenService = tokenService;
        setFilterProcessesUrl("/member/v1/login");
    }

//    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        // --> 로그인 폼에 대한 예외처리도 추가..해야함...
        Member member = objectMapper.readValue(request.getInputStream(), Member.class);

        //refresh토큰 포함 여부를 쿠키에서 가지고옴
        // 최초 로그인 전에는 refresh토큰을 가지고 있지 않기 때문에, refresh토큰을 가지고 요청 들어오지 않음.
        String refresh_tokenVal = "";
        Cookie[] cookies = request.getCookies();

        Map<String, String> targetCookie = new HashMap<>();

        for (Cookie cookie : cookies) {
            targetCookie.put(cookie.getName(), cookie.getValue());
        }
        if(targetCookie.get("refresh_token") != null) {
            String cookieResult = targetCookie.get("refresh_token");
            refresh_tokenVal = cookieResult.substring("Bearer ".length());
        }
        if(refresh_tokenVal.isBlank()) {
            // 리프레시 토큰을 가지고 있지 않으면, 토큰 생성
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    member.getMemberId(), member.getPwd(), null
            );

            // 매니저에게 토큰 검증해달라고 요청 , 다오 어센티케이터 프로바이더를 통해서 유저디테일즈 서비스에서 검증해줌
            return getAuthenticationManager().authenticate(token);
        } else {
            //refreshToken이 왔다면
//            VerifyResult verify = JWTUtil.verify(member.getRefreshToken());
            VerifyResult verify = JWTUtil.verify(refresh_tokenVal);
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
//        response.setHeader("refresh_token", JWTUtil.makeRefreshToken(details));

        String refresh_token = "Bearer "+JWTUtil.makeRefreshToken(details);
        refresh_token = URLEncoder.encode(refresh_token);

        Cookie cookie = new Cookie("refresh_token",refresh_token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(details));

        // --------------------refresh Token 저장 -----------------------------
        TokenDTO tokenDTO = TokenDTO.builder()
                .userName(details.getUsername())
                .refresh_token(refresh_token.substring("Bearer ".length()))
                .build();
        tokenService.saveRefreshToken(tokenDTO);

    }
}