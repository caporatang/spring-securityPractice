package com.trading.day.member.repository;

import com.trading.day.member.domain.Member;
import com.trading.day.member.domain.TokenManage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName : com.trading.day.member.repository
 * fileName : TokenManageJpaRepository
 * author : taeil
 * date : 2022/12/18
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/18        taeil                   최초생성
 */
public interface TokenManageJpaRepository extends JpaRepository<TokenManage, Long> {

    Optional<TokenManage> findByMemberEntityMemberNo(Long memberNo);

}