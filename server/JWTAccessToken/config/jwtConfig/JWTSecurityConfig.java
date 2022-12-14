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
// * 2022/12/15        taeil                   ????????????
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
//        // ???????????? ????????? ?????????????????? ????????? ?????? ????????? ?????? ?????? ????????????.
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
//        return roleHierarchy;
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JWTLoginFilter loginFilter = new JWTLoginFilter(authenticationManager(), memberService, tokenService);
        JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(), memberService);

        // ????????? ???????????????,
        //csrf??? ???????????????,csrf????????? ???????????? ????????? ???????????? ???????????? ????????? ??????????????? ????????? ???
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
    //cors ??????
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