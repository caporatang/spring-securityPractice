package com.trading.day.config.jwtConfig;

import com.trading.day.member.domain.Member;
import com.trading.day.member.domain.MemberDTO;
import com.trading.day.member.domain.Role;
import com.trading.day.member.domain.UserRole;
import com.trading.day.member.repository.MemberJpaRepository;
import com.trading.day.member.repository.RoleJpaRepository;
import com.trading.day.member.repository.UserRoleJpaRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

/**
 * packageName : com.trading.day.config.jwtConfig
 * fileName : CustomUserDtails
 * author : taeil
 * date : 2022/12/15
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/15        taeil                   최초생성
 */
@Service
@Transactional
public class CustomUserDtailsService implements UserDetailsService {

    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private RoleJpaRepository roleRepository;
    @Autowired
    private UserRoleJpaRepository userRoleJpaRepository;

    @Autowired
    private RoleJpaRepository roleJpaRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        return  memberJpaRepository.findMemberByMemberId(memberId).orElseThrow(
                () -> new UsernameNotFoundException(memberId));
    }


    public MemberDTO save2(MemberDTO inDto) {

        //Entity 변환 작업
        Member member = modelMapper.map(inDto, Member.class);
        member.setCreateDate(LocalDateTime.now());
        member.setModifiedDate(LocalDateTime.now());

        Optional<Role> role = roleJpaRepository.findById(1L); //1L 값 = USER

        // 실제 사용 코드 user Role Table
        UserRole userRole = new UserRole();
        userRole.setMember(member); // 저장할 MEMBER Entity 객체 -- PARAM : MEMBER ENTITY - TYPE ENTITY

        userRole.setRoleId(role.get()); // 실사용 코드
        userRole.setCreatedDate(LocalDateTime.now());
        userRole.setModifiedDate(LocalDateTime.now());

        UserRole saveUserRole = userRoleJpaRepository.save(userRole); // UserRole 저장.

        //실제 사용코드  멤버 save - UserRole에 대한 Target 테이블은 나중에 insert
        Member save = memberJpaRepository.save(member); //
        save.addUserRoles(saveUserRole); // Member Entity에 UserRoles에 대한 정보 add

        // Entity 결과 to DTO
        MemberDTO out = modelMapper.map(save, MemberDTO.class);

        return out;
    }




}