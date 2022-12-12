package com.sp.fc.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class JWTSimpleTest {

    private void printToken(String token) {
        String[] tokens = token.split("\\.");
        //header에는 보통 말고리즘 값 "alg : HS256"
        System.out.println("header : " + new String(Base64.getDecoder().decode(tokens[0])));
        //body에는 보통 사용자가 추가한 데이터가 들어간다. "alg : HS256"
        System.out.println("body : " + new String(Base64.getDecoder().decode(tokens[1])));
    }


    @DisplayName("1. jjwt 를 이용한 토큰 테스트")
    @Test
    void test_1(){ // okta
        String okta_token = Jwts.builder().addClaims(
                Map.of("name", "taeil", "price", 3000)
        ).signWith(SignatureAlgorithm.HS256, "taeil")
                .compact(); // --> 토큰 인코딩
        System.out.println(okta_token);
        printToken(okta_token);

        Jws<Claims> tokenInfo = Jwts.parser().setSigningKey("taeil").parseClaimsJws(okta_token);
        System.out.println(tokenInfo);
    }


    @DisplayName("2. java-jwt 를 이용한 토큰 테스트")
    @Test
    void test_2() { // oauth

        byte[] SEC_KEY = DatatypeConverter.parseBase64Binary("taeil");

        String oauth0_token = JWT.create().withClaim("name", "taeil").withClaim("price", 3000)
                .sign(Algorithm.HMAC256(SEC_KEY));
        System.out.println(oauth0_token);
        printToken(oauth0_token);

        DecodedJWT verified = JWT.require(Algorithm.HMAC256(SEC_KEY)).build().verify(oauth0_token);
        System.out.println("veryfied" + verified.getClaims());

        Jws<Claims> tokenInfo = Jwts.parser().setSigningKey(SEC_KEY).parseClaimsJws(oauth0_token);
        System.out.println("tokenInfo : " + tokenInfo);
    }

    @DisplayName("3. 만료 시간 테스트")
    @Test
    void test_3 () throws InterruptedException {
        final Algorithm AL = Algorithm.HMAC256("taeil");

        //토큰 유효시간은 3초
        String token = JWT.create().withSubject("a1234")
                .withNotBefore(new Date(System.currentTimeMillis() + 1000))
                .withExpiresAt(new Date(System.currentTimeMillis() + 3000))
                .sign(AL);


//        Thread.sleep(2000);
        try {
            DecodedJWT verify = JWT.require(AL).build().verify(token);
            System.out.println(verify.getClaims());
        } catch (Exception ex) {
            System.out.println("유효하지 않은 토큰입니다...~");
            DecodedJWT decoded = JWT.decode(token);
            System.out.println(decoded.getClaims());
        }


    }


}
