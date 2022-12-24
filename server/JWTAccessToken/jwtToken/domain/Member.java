package com.trading.day.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.trading.day.config.BaseTimeEntity;
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
public class Member extends BaseTimeEntity {

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

//    @CreatedDate
//    private LocalDateTime createDate;      /* 가입 날짜 */
//    @CreatedDate
//    private LocalDateTime modifiedDate;     /* 수정 날짜 */

    // Item 게시판 Mapping
    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<ItemBoard> itemBoards = new ArrayList<>();

    // Qna mapping
    @JsonIgnore // --> Json으로 변환 과정중에 무한으로 참조가 순환문제를 해결 --> 무한 순환을 끊어줌
    @OneToMany(mappedBy = "member")
    private List<Qna> qnas = new ArrayList<>();

    // Apply (지원서)
    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Apply> applys = new ArrayList<>();

    public void addApplys(Apply apply) {
        apply.setMember(this);
        applys.add(apply);
    }

    public void addQnas (Qna qna) {
        qnas.add(qna);
        qna.setMember(this);
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_no", foreignKey = @ForeignKey(name="member_no"))
    @JsonIgnore
    private Set<UserRole> authorities;

    // Item Board add
    public void addItemBoards(ItemBoard itemBoard) {
        itemBoards.add(itemBoard);
        itemBoard.setMember(this);
    }

    //user details
    @Column(name = "activated")
    private final boolean activated = true;

}