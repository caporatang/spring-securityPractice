package com.sp.fc.web;

import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.repository.SpUserRepository;
import com.sp.fc.user.service.SpUserService;
import com.sp.fc.web.config.UserLoginForm;
import com.sp.fc.web.test.WebIntegrationTest;
import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

//WebIntegrationTest --> springbootTest, port, url을 서비스해줌
public class JWTRequestTest extends WebIntegrationTest {

    @Autowired
    private SpUserRepository userRepository;
    @Autowired
    private SpUserService spUserService;

    @BeforeEach
    void before() {
        userRepository.deleteAll();

        SpUser user = spUserService.save(SpUser.builder()
                .email("user1")
                .password("1111")
                .enabled(true)
                .build());
        spUserService.addAuthority(user.getUserId(), "ROLE_USER");
    }

    @DisplayName("1. hello 메시지를 받아옴")
    @Test
    void test_1 () {
        RestTemplate client = new RestTemplate();

        // 클라이언트에서 넘어온 데이터가 바디에 들어왔음을 가정함
        HttpEntity<UserLoginForm> body = new HttpEntity<>(
                UserLoginForm.builder()
                        .username("user1")
                        .password("1111")
                        .build()
        );

        // 맞는 유저라면, SpUser클래스를 리턴
        ResponseEntity<SpUser> resp1 = client.exchange(uri("/login"), HttpMethod.POST, body, SpUser.class);
        System.out.println(resp1.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0));
        System.out.println(resp1.getBody());

        // 메서드 접근
//        HttpHeaders header = new HttpHeaders();
//        header.add(HttpHeaders.AUTHORIZATION, resp1.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0));
//        body = new HttpEntity<>(null, header);
//        ResponseEntity<String> resp2 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);
//
//        System.out.println(resp2);


        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, resp1.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0));
        body = new HttpEntity<>(null, header);
        ResponseEntity<String> resp2 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);
        //ResponseEntity<String> resp2 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);
        System.out.println(resp2);
        assertEquals("hello", resp2.getBody());

    }


}
