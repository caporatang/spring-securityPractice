package com.sp.fc.web.config;

import com.sp.fc.user.service.SpUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * packageName : com.sp.fc.web.config
 * fileName : AdvancedSecurityConfig
 * author : taeil
 * date : 2022/12/12
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/12        taeil                   최초생성
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdvancedSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SpUserService userService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //로그인필터
        // authenticationManager에게 위임가능
        JWTLoginFilter loginFilter = new JWTLoginFilter(authenticationManager(), userService);
        // 검증 필터
        JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(), userService);

        http
                .csrf().disable() // --> jwt는 비용이 많이 들기 때문에 디스에이블
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //UsernamePasswordAuthenticationFilter.class를 만든 loginfilter로 대체함
                // 로그인을 해줌
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                // 체크 필터로 BasicAuthenticationFilter 대체함
                // 검증을 해줌
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class)
                ;
    }
}