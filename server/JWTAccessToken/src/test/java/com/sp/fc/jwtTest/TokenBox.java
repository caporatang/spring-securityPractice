package com.trading.day.jwtTest;

import lombok.*;

/**
 * packageName : com.trading.day.jwtTest
 * fileName : TokenBox
 * author : taeil
 * date : 2022/12/17
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/17        taeil                   최초생성
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TokenBox {
    private String authToken;
    private String refreshToken;
}