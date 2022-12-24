package com.trading.day.member.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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

    private LocalDateTime createdDate;   /* 가입 날짜 */
    private LocalDateTime modifiedDate;  /* 수정 날짜 */

}
