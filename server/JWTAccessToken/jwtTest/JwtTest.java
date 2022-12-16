package com.trading.day.jwtTest;


import com.trading.day.config.jwtConfig.UserLoginForm;
import com.trading.day.member.domain.Member;
import com.trading.day.member.domain.MemberDTO;
import com.trading.day.member.repository.MemberJpaRepository;
import com.trading.day.member.repository.UserRoleJpaRepository;
import com.trading.day.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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

//    @Autowired
//    private CustomUserDtailsService customUserDtailsService;

    RestTemplate client = new RestTemplate();

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


    @DisplayName("1. 로그인이나 좀 되라")
    @Test
    void test_1() {

        TestRestTemplate testRestTemplate = new TestRestTemplate();

        HttpEntity<MemberDTO> body = new HttpEntity<>(
                MemberDTO.builder().memberId("user2").pwd("2222").build()
        );
        //ResponseEntity<MemberDTO> resp1 = client.exchange(uri("/login"), HttpMethod.POST, body, MemberDTO.class);
        ResponseEntity<Member> resp1 = client.exchange(uri("/login"), HttpMethod.POST, body, Member.class);
        System.out.println("------------------- 엑세스 토큰 -----------------");

        System.out.println("나는 토큰 정보 : " + resp1);
        System.out.println("나는 Bearer토큰 : " + resp1.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0));

        System.out.println("------------------- 엑세스 토큰 -----------------");

//        HttpHeaders header = new HttpHeaders();
//        header.add(HttpHeaders.AUTHORIZATION, resp1.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0));
//        body = new HttpEntity<>(null, header);
//
//        ResponseEntity<String> resp2 = client.exchange(uri("/member/v1/greeting"), HttpMethod.GET, body, String.class);
//        System.out.println("로그인 후 헬로 메서드에서 읽어옴 @@ : " + resp2.getBody());
//        assertEquals("hello", resp2.getBody());



    }
}