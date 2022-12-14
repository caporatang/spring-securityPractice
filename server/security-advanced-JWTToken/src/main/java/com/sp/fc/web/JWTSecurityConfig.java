//package com.trading.day.config.jwtConfig;
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
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class JWTSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private MemberService memberService;
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        JWTLoginFilter loginFilter = new JWTLoginFilter(authenticationManager());
//        JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(), memberService);
//
//        // 토큰을 사용하려면,
//        //csrf를 디스에이블,csrf토큰을 서버에서 로컬에 심어놓고 왔다갔다 하면서 통신하기엔 비용이 큼
//        http
//                .csrf().disable()
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterAt(checkFilter, BasicAuthenticationFilter.class);
//
//        http
//                .authorizeHttpRequests().antMatchers("**/member/v1/**").permitAll();
//    }
//}