package com.trading.day.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;



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
@RequiredArgsConstructor
public class TokenManage {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long tokenId;

    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member memberEntity;

    @Builder
    public TokenManage(String refreshToken, Member memberEntity) {
        this.refreshToken = refreshToken;
        this.memberEntity = memberEntity;
    }

    // 사용하는 refreshToken이 유효시간이 만료되었을 때 DB에 업데이트
    public void refreshUpdate(String refreshToken) {
        this.refreshToken = refreshToken;
    }


}