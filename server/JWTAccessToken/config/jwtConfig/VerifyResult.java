package com.trading.day.config.jwtConfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName : com.trading.day.config.jwtConfig
 * fileName : VerifyResult
 * author : taeil
 * date : 2022/12/15
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/15        taeil                   최초생성
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyResult {
// 인증 결과
    private boolean success;
    private String memberId;
}