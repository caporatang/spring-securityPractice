package com.sp.fc.web;

import com.sp.fc.web.student.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MultiChainProxyTest {
    @LocalServerPort
    int port;

    //basic 토큰을 가져갈것을 명시
    TestRestTemplate testClient = new TestRestTemplate("choi", "1");

    @DisplayName("1. choi:1 으로 로그인하여 학생 리스트를 내려 받는다.")
    @Test
    void test_1 () {

        ResponseEntity<List<Student>> resp =  testClient.exchange("http://localhost:" + port + "/api/teacher/studentslist",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
                });
        System.out.println("@@@@@" + resp.getBody());
//        assertNotNull(resp.getBody());
//        assertEquals(3, resp.getBody().size());


    }
}
