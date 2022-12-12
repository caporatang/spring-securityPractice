package com.sp.fc.web.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName : com.sp.fc.web.config
 * fileName : UserLoginForm
 * author : taeil
 * date : 2022/12/13
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/13        taeil                   최초생성
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginForm {

    private String username;
    private String password;
    private String refreshToken;

}