package com.sp.fc.web.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName : com.sp.fc.web.config
 * fileName : VerifyResult
 * author : taeil
 * date : 2022/12/12
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/12        taeil                   최초생성
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyResult {
    // jwt 유효성 검증함수
    private boolean success;
    private String username;

}