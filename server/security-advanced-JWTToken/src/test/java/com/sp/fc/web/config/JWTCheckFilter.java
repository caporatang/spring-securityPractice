package com.trading.day.config.jwtConfig;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.trading.day.jwtToken.domain.TokenManage;
import com.trading.day.jwtToken.repository.TokenManageJpaRepository;
import com.trading.day.member.service.MemberService;
import org.apache.el.parser.TokenMgrError;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    private TokenManageJpaRepository tokenManageJpaRepository;
    public JWTCheckFilter(AuthenticationManager authenticationManager, MemberService memberService, TokenManageJpaRepository tokenManageJpaRepository) {
        super(authenticationManager);
        this.memberService = memberService;
        this.tokenManageJpaRepository = tokenManageJpaRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 헤더에 bearer토큰이 있을수도 없을수도 있고,
        if (bearer == null || !bearer.startsWith("Bearer ")) {
            // 인증이 필요없을수도있기 때문에 흘려보냄
            chain.doFilter(request, response); //
        } else {

            String token = bearer.substring("Bearer ".length());
            VerifyResult result = JWTUtil.verify(token);

            if (result.isSuccess()) {
                // 유효성이 검증된다면, 조회한 유저 디테일의 데이터로 토큰을 직접 만듬
                UserDetails user = memberService.loadUserByUsername(result.getMemberId());

                UsernamePasswordAuthenticationToken memberToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, user.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(memberToken);
                super.doFilterInternal(request, response, chain);

            } else {
                // auth_token이 만료인경우, refresh토큰으로 유효성검증을 다시..
                // cookie에서 가져와서 하는 이유는 보안의 이유로 httpOnly, secure옵션을 주었기 때문에 프론트(js)에서 접근이 불가하기 때문이다.
                Cookie[] cookies = request.getCookies();
                Map<String, String> targetCookie = new HashMap<>();

                for (Cookie cookie : cookies) {
                    targetCookie.put(cookie.getName(), cookie.getValue());
                }
                String cookieResult = targetCookie.get("refresh_token");

                token = cookieResult.substring("Bearer ".length());
                result = JWTUtil.verify(token);

                UserDetails user = memberService.loadUserByUsername(result.getMemberId());

                Optional<TokenManage> findDBTokenEntity = tokenManageJpaRepository.findByUserName(result.getMemberId());
                String findDBResult = findDBTokenEntity.get().getRefreshToken();

                if(findDBResult.equals(token)) {
                    if (result.isSuccess()) {
//                    UserDetails user = memberService.loadUserByUsername(result.getMemberId());
                        user = memberService.loadUserByUsername(result.getMemberId());

                        UsernamePasswordAuthenticationToken memberToken = new UsernamePasswordAuthenticationToken(
                                user.getUsername(), null, user.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(memberToken);
                        super.doFilterInternal(request, response, chain);
                    } else {
                        throw new TokenExpiredException("refresh_token is Expired");
                    }
                } else {
                    throw new AuthenticationException("refreshToken is not valid");
                }
            }
        }
    }
} // CheckFilter end