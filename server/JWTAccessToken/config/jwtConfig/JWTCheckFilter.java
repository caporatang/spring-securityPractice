package com.trading.day.config.jwtConfig;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.trading.day.member.service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * packageName : com.trading.day.config.jwtConfig
 * fileName : JWTCheckFilter
 * author : taeil
 * date : 2022/12/15
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/15        taeil                   최초생성
 */
public class JWTCheckFilter extends BasicAuthenticationFilter {
    // 컨텍스트 홀더에 데이터(유저정보)를 넣어줄것.

    private MemberService memberService;


    public JWTCheckFilter(AuthenticationManager authenticationManager, MemberService memberService) {
        super(authenticationManager);
        this.memberService = memberService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 헤더에 bearer토큰이 있을수도 없을수도 있고,
        if(bearer == null || !bearer.startsWith("Bearer ")) {
            // 인증이 필요없을수도있기 때문에 흘려보냄
            chain.doFilter(request, response); //
        } else {
            String token = bearer.substring("Bearer ".length());
            VerifyResult result = JWTUtil.verify(token);

            if(result.isSuccess()) {
                // 유효성이 검증된다면, 조회한 유저 디테일의 데이터로 토큰을 직접 만듬
                UserDetails user = memberService.loadUserByUsername(result.getMemberId());

                UsernamePasswordAuthenticationToken memberToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, user.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(memberToken);
                super.doFilterInternal(request, response, chain);
            } else {
//            throw new AuthenticationException("JWTCheckFilter 401 : token is not valid");
                throw  new TokenExpiredException("token is Expired");
            }
        }



    }



} // CheckFilter end