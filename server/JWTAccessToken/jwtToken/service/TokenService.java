package com.trading.day.jwtToken.service;

import com.trading.day.jwtToken.domain.TokenDTO;
import com.trading.day.jwtToken.domain.TokenManage;
import com.trading.day.jwtToken.repository.TokenManageJpaRepository;
import com.trading.day.member.domain.Member;
import com.trading.day.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Table;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;


    //public void saveRefreshToken (TokenManage tokenManage) {
    public void saveRefreshToken (TokenDTO tokenDTO) {
        try {
//            Optional<TokenManage> findTokenData =
//                    Optional.ofNullable(tokenManageJpaRepository.findByUserName(tokenDTO.getUserName()).orElseThrow(
//                            () -> new IllegalArgumentException("검색 결과가 없습니다")));

            Optional<TokenManage> findTokenData = tokenManageJpaRepository.findByUserName(tokenDTO.getUserName());

            if(ObjectUtils.isEmpty(findTokenData)) {

                Member memberResult = memberJpaRepository.findByMemberId(tokenDTO.getUserName());
                Long memberNo = memberResult.getMemberNo();

                tokenDTO.setMemberNo(memberNo);
                TokenManage tokenManage = modelMapper.map(tokenDTO, TokenManage.class);
                tokenManageJpaRepository.save(tokenManage);
            }
        } catch (Exception exception) {
            throw new IllegalArgumentException("refresh 토큰 저장 실패");
        }
    }

}