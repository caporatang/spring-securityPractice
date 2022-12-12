package com.sp.fc.web.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.service.SpUserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * packageName : com.sp.fc.web.config
 * fileName : JWTCheckFilter
 * author : taeil
 * date : 2022/12/12
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/12        taeil                   최초생성
 */
// 검사를해서 시큐리티컨텍스트홀더에 유저 프린서펄을 채워주는 역할을할것.
public class JWTCheckFilter extends BasicAuthenticationFilter {

    private SpUserService userService;

    public JWTCheckFilter(AuthenticationManager authenticationManager, SpUserService userService) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 토큰을 조사함
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 인증이 필요없을수도있고, Bearer 토큰이 아닐수 있기 떄문에 그냥 흘려보냄
        if (bearer == null || !bearer.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        // bearer 토큰이라면 토큰을 가져와야함
        String token = bearer.substring("Bearer ".length());
        VerifyResult result = JWTUtil.verify(token);

        if(result.isSuccess()) {
            // 맞다면 유저네임과 유저가 가지고 있는 권한으로 네임패스워드 토큰을 생섬함.
            SpUser user = (SpUser) userService.loadUserByUsername(result.getUsername());
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(userToken);
            //정상적이면 설정한 필터를 태워보냄
            chain.doFilter(request, response);
        } else {
            // 아닌 경우 --> 401 에러
            throw new TokenExpiredException("Token is not valid");
        }
    }
}