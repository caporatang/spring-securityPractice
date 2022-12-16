package com.trading.day.config.jwtConfig;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.trading.day.member.domain.Member;
import com.trading.day.member.domain.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;

/**
 * packageName : com.trading.day.config.jwtConfig
 * fileName : JWTUtil
 * author : taeil
 * date : 2022/12/14
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/14        taeil                   최초생성
 */
public class JWTUtil {

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("TradingDay");

    //인증토큰의 유효시간은 20분
    private static final long AUTH_TIME = 10 * 60;

    //리프레시 토큰은 일주일
    private static final long REFRESH_TIME = 60 * 60 * 24 * 7;

//    public static String makeAuthToknen(MemberDTO memberDTO) {
//        return JWT.create().withSubject(memberDTO.getMemberId())
//                .withClaim("exp", Instant.now().getEpochSecond() + AUTH_TIME)
//                .sign(ALGORITHM);
//    }
//
//    public static String makeRefreshToken(MemberDTO memberDTO) {
//        return JWT.create().withSubject(memberDTO.getMemberId())
//                .withClaim("exp", Instant.now().getEpochSecond() + REFRESH_TIME)
//                .sign(ALGORITHM);
//    }
public static String makeAuthToknen(UserDetails details) {
    return JWT.create().withSubject(details.getUsername())
            .withClaim("exp", Instant.now().getEpochSecond() + AUTH_TIME)
            .sign(ALGORITHM);
}

//    public static String makeRefreshToken(UserDetails details) {
//        return JWT.create().withSubject(member.getMemberId())
//                .withClaim("exp", Instant.now().getEpochSecond() + REFRESH_TIME)
//                .sign(ALGORITHM);
//    }

    // 토큰의 유효성을 검증함
    public static VerifyResult verify(String token) {
        try {
            DecodedJWT verify =  JWT.require(ALGORITHM).build().verify(token);
            return VerifyResult.builder()
                    .success(true)
                    .memberId(verify.getSubject())
                    .build();

        } catch (Exception e ) {
            DecodedJWT decoded = JWT.decode(token);
            return VerifyResult.builder()
                    .success(false)
                    .memberId(decoded.getSubject())
                    .build();
        }

    }


}