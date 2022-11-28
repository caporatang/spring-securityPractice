package com.sp.fc.web.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * packageName : com.sp.fc.web.student
 * fileName : Student
 * author : taeil
 * date : 2022/11/28
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/11/28        taeil                   최초생성
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
    //principal
    private String id;
    private String username;
    private Set<GrantedAuthority> role;
}