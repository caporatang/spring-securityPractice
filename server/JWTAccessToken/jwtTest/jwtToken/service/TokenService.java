package com.trading.day.jwtToken.service;

import com.trading.day.jwtToken.domain.TokenDTO;
import com.trading.day.jwtToken.domain.TokenManage;
import com.trading.day.jwtToken.repository.TokenManageJpaRepository;
import com.trading.day.member.domain.Member;
import com.trading.day.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Table;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.swing.text.html.Option;
import javax.xml.transform.Result;
import java.util.Optional;

/**
 * packageName : com.trading.day.jwtToken.service
 * fileName : TokenService
 * author : taeil
 * date : 2022/12/19
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/19        taeil                   최초생성
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final TokenManageJpaRepository tokenManageJpaRepository;
    private final MemberJpaRepository memberJpaRepository;


    public void saveRefreshToken (TokenManage tokenManage) {

        try {

            Member memberResult = memberJpaRepository.findByMemberId(tokenManage.getUserName());
            tokenManage.setMemberEntity(memberResult);
            TokenManage tokenResult = tokenManageJpaRepository.save(tokenManage);

            System.out.println("tokenService@@@@ : " + tokenResult);

        } catch (Exception exception) {
            if (ObjectUtils.isEmpty(tokenManage)) {
                throw new NullPointerException("TokenServcie : 데이터를 찾을 수 없거나 입력값이 없어요");
            } else if(ObjectUtils.isEmpty(tokenManage.getUserName())) {
                throw new NullPointerException("TokenServcie : 유저아이디가 없습니다");
            }  else if(ObjectUtils.isEmpty(tokenManage.getRefreshToken())) {
                throw new NullPointerException("TokenServcie : refreshToken이 없습니다");
            }
        }
    }

}