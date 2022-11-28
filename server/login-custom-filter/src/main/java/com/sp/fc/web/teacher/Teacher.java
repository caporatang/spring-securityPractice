package com.sp.fc.web.teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * packageName : teacher
 * fileName : Teacher
 * author : taeil
 * date : 2022/11/28
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/11/28        taeil                   최초생성
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Teacher {
    //principal
    private String id;
    private String username;
    private Set<GrantedAuthority> role;

}