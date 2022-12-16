package com.trading.day.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.trading.day.item.apply.domain.Apply;
import com.trading.day.item.domain.ItemBoard;
import com.trading.day.qna.domain.Qna;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Slf4j
@NoArgsConstructor
@Table(name = "MEMBER")
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class Member implements UserDetails {
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_no")
    private Long memberNo;                 /* 고객번호 */
    private String memberId;               /* 고객 ID */
    private String name;                   /* 이름 */
    private String email;                  /* 이메일*/
    private String telNo;                  /* 전화번호 */
    private String address;                /* 주소 */
    private String pwd;                     /*비밀번호*/

    @CreatedDate
    private LocalDateTime createDate;      /* 가입 날짜 */
    @CreatedDate
    private LocalDateTime modifiedDate;     /* 수정 날짜 */

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_no", foreignKey = @ForeignKey(name="member_no"))
    @JsonIgnore
    private Set<UserRole> authorities;

    //user details
    @Column(name = "activated")
    private final boolean activated = true;





}