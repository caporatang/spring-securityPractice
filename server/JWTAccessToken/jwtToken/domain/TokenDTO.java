package com.trading.day.jwtToken.domain;

import lombok.*;

/**
 * packageName : com.trading.day.jwtToken.domain
 * fileName : TokenDTO
 * author : taeil
 * date : 2022/12/18
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/18        taeil                   최초생성
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    // 클라이언트와 request, response DTO

    private String tokenId;
    private String userName;
    private Long memberNo;
    private String refresh_token; // --> 리프레시토큰
    private String auth_token; // 엑세스 토큰

}