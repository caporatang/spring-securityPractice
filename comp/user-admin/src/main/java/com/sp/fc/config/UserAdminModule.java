package com.sp.fc.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * packageName : com.sp.fc.config
 * fileName : UserAdminModule
 * author : taeil
 * date : 2022/12/05
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/05        taeil                   최초생성
 */
@Configuration
@ComponentScan("com.sp.fc.user")
@EntityScan(basePackages = {
        "com.sp.fc.user.domain"
})
@EnableJpaRepositories(basePackages = {
        "com.sp.fc.user.repository"
})
public class UserAdminModule {
}