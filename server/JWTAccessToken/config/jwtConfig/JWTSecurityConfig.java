package com.trading.day.config.jwtConfig;//package com.trading.day.config.jwtConfig;

import com.trading.day.jwtToken.domain.TokenManage;
import com.trading.day.jwtToken.service.TokenService;
import com.trading.day.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

//
//import com.trading.day.member.service.MemberService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
///**
// * packageName : com.trading.day.config.jwtConfig
// * fileName : JWTSecurityConfig
// * author : taeil
// * date : 2022/12/15
// * description :
// * =======================================================
// * DATE          AUTHOR                      NOTE
// * -------------------------------------------------------
// * 2022/12/15        taeil                   최초생성
// */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JWTSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MemberService memberService;
    @Autowired
    private TokenService tokenService;

    @Bean
    PasswordEncoder passwordEncoder() {
        //testOnly
        return NoOpPasswordEncoder.getInstance();
    }

//    @Bean
//    RoleHierarchy roleHierarchy () {
//        // 어드민의 권한을 가지고있으면 유저가 가진 권한도 전부 수행 가능하다.
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
//        return roleHierarchy;
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JWTLoginFilter loginFilter = new JWTLoginFilter(authenticationManager(), memberService, tokenService);
        JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(), memberService);

        // 토큰을 사용하려면,
        //csrf를 디스에이블,csrf토큰을 서버에서 로컬에 심어놓고 왔다갔다 하면서 통신하기엔 비용이 큼
        http
                .authorizeRequests()
//        .antMatchers("/swagger-resources/**").permitAll().anyRequest().fullyAuthenticated()
        .antMatchers("/qna/v1/**", "/member/v1/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .cors().and()
                .csrf().disable()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class);

    }
    //cors 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

//        configuration.addAllowedOrigin("*");
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                // -- Static resources
                "/css/**", "/images/**", "/js/**"
                // -- Swagger UI v2
                , "/v2/api-docs", "/swagger-resources/**"
                , "/swagger-ui.html", "/webjars/**", "/swagger/**"
        );
    }



}