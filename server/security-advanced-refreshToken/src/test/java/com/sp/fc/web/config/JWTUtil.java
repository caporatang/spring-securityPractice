package com.sp.fc.web.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sp.fc.user.domain.SpUser;

import java.time.Instant;

/**
 * packageName : com.sp.fc.web.config
 * fileName : JWTUtil
 * author : taeil
 * date : 2022/12/12
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/12        taeil                   최초생성
 */
public class JWTUtil {

    // jwt토큰을 만들어주기 위한 jwt토큰
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("taeil");
    private static final long AUTH_TIME = 2;
    private static final long REFRESH_TIME = 60 * 60 * 24 * 7;

    public static String makeAuthToken(SpUser user) {
        // 토큰 만들기
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("exp", Instant.now().getEpochSecond()+ AUTH_TIME)
                .sign(ALGORITHM);
    }

    public static String makeRefreshToken(SpUser user) {
        // refresh 토큰 만들기
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("exp", Instant.now().getEpochSecond()+ REFRESH_TIME)
                .sign(ALGORITHM);
    }


    public static VerifyResult verify(String token) {
        // 토큰의 유효성 검증
        try {
            DecodedJWT verify =  JWT.require(ALGORITHM).build().verify(token);
            return VerifyResult.builder().success(true)
                    .username(verify.getSubject()).build();
        } catch (Exception e) {
            DecodedJWT decoded = JWT.decode(token);
            return VerifyResult.builder().success(false)
                    .username(decoded.getSubject()).build();
        }
    }
}