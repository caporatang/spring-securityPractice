package com.trading.day.jwtToken.domain;

import com.trading.day.config.BaseTimeEntity;
import com.trading.day.member.domain.Member;
import lombok.*;


import javax.persistence.*;

/**
 * packageName : com.trading.day.member.domain
 * fileName : TokenManage
 * author : taeil
 * date : 2022/12/18
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/18        taeil                   최초생성
 */
@Entity
@Getter
@Setter
@Table(name = "TOKEN_MANAGE")
public class TokenManage extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long tokenId;

    private String userName;
    private String refreshToken;
    private Long memberNo;
//    @ManyToOne
//    @JoinColumn(name = "member_no")
//    private Member memberEntity;

    // 사용하는 refreshToken이 유효시간이 만료되었을때 DB에 업데이트
    public void refreshUpdate(String refreshToken) {
        this.refreshToken = refreshToken;
    }


}