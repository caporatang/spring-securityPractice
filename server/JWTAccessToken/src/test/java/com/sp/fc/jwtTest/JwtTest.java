package com.trading.day.jwtTest;


import antlr.Token;
import com.trading.day.member.domain.Member;
import com.trading.day.member.domain.MemberDTO;
import com.trading.day.member.repository.MemberJpaRepository;
import com.trading.day.member.repository.UserRoleJpaRepository;
import com.trading.day.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * packageName : com.trading.day.jwtTest
 * fileName : JwtTest
 * author : taeil
 * date : 2022/12/15
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/15        taeil                   최초생성
 */

@TestPropertySource(properties = "spring.jpa.properties.hibernate.default_batch_fetch_size=1000")
public class JwtTest extends WebIntegrationTest{
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private UserRoleJpaRepository userRoleJpaRepository;

    @BeforeEach
    void before() {
        userRoleJpaRepository.deleteAll();
        memberJpaRepository.deleteAll();

        Member member = new Member();
        member.setMemberId("user2");
        member.setPwd("2222");
        Member result = memberService.save2(member);
        memberService.addAuthority(result.getMemberNo(), "ROLE_USER");
    }

    private TokenBox getToken() { // -----------> test Only
        RestTemplate client = new RestTemplate();
        HttpEntity<MemberDTO> body = new HttpEntity<>(
                MemberDTO.builder().memberId("user2").pwd("2222").build()
        );
        ResponseEntity<Member> resp1 = client.exchange(uri("/login"), HttpMethod.POST, body, Member.class);
        System.out.println("resp1@@@ : " + resp1);

        return TokenBox.builder().authToken(resp1.getHeaders().get("auth_token").get(0))
                .refreshToken(resp1.getHeaders().get("refresh_token").get(0))
                .build();
    }

    private TokenBox refreshToken(String refreshToken) { // -----------> test Only

        RestTemplate client = new RestTemplate();
        HttpEntity<MemberDTO> body = new HttpEntity<>(
                MemberDTO.builder().refreshToken(refreshToken).build()
        );
        ResponseEntity<Member> resp1 = client.exchange(uri("/login"), HttpMethod.POST, body, Member.class);

        // 토큰을 리턴
        return TokenBox.builder().authToken(resp1.getHeaders().get("auth_token").get(0))
                .refreshToken(resp1.getHeaders().get("refresh_token").get(0))
                .build();
    }

    @DisplayName("1. 로그인")
    @Test
    void test_1() {
        TokenBox token = getToken();

        RestTemplate client = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, "Bearer "+token.getAuthToken());

        HttpEntity body = new HttpEntity<>(null, header);
        ResponseEntity<String> resp2 = client.exchange(uri("/member/v1/greeting"), HttpMethod.GET, body, String.class);
        System.out.println("resp2@@@" + resp2.getBody());
        assertEquals("hello", resp2.getBody());
    }

    @DisplayName("토큰 만료 refresh_Token Test")
    @Test
    void test_2() throws InterruptedException {
        TokenBox token = getToken();

        Thread.sleep(3000);

        RestTemplate client = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, "Bearer "+token.getAuthToken());

        assertThrows(Exception.class, () ->{
            HttpEntity body = new HttpEntity<>(null, header);
            ResponseEntity<String> resp2 = client.exchange(uri("/member/v1/greeting"), HttpMethod.GET, body, String.class);
        });

        token = refreshToken(token.getRefreshToken());
        HttpHeaders header2 = new HttpHeaders();
        // bearer 토큰을 갱신된 auth 토큰으로 변경하여 다시 요청을 보낸다
        header2.add(HttpHeaders.AUTHORIZATION, "Bearer "+token.getAuthToken());
        HttpEntity body = new HttpEntity<>(null, header2);
        ResponseEntity<String> resp3 = client.exchange(uri("/member/v1/greeting"), HttpMethod.GET, body, String.class);
        System.out.println("resp3.getBody" + resp3.getBody());
        assertEquals("hello", resp3.getBody());



    } // test end
} // class end