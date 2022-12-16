package com.trading.day.member.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
//public class MemberDTO implements UserDetails{
public class MemberDTO {

    private Long memberNo;              /* 고객번호 */
    private String memberId;            /* 고객 ID */
    private String name;                /* 이름 */
    private String email;               /* 이메일 */
    private String telNo;               /* 전화번호 */
    private String address;                /* 주소 */
    private String pwd;                     /*비밀번호*/


    private LocalDateTime createDate;   /* 가입 날짜 */
    private LocalDateTime modifiedDate;  /* 수정 날짜 */

//    private Collection<GrantedAuthority> authorities;
//    private boolean enabled;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return pwd;
//    }
//
//    @Override
//    public String getUsername() {
//        return memberId;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return enabled;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return enabled;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return enabled;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }




}
