package com.trading.day.member.controller;

import com.trading.day.config.jwtConfig.JWTLoginFilter;
import com.trading.day.config.jwtConfig.JWTUtil;
import com.trading.day.jwtToken.domain.ResponseTokenDTO;
import com.trading.day.jwtToken.domain.TokenDTO;
import com.trading.day.member.domain.Member;
import com.trading.day.member.domain.MemberDTO;
import com.trading.day.member.service.MemberService;
import com.trading.day.qna.domain.Qna;
import com.trading.day.qna.domain.QnaDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

/************
* @info : 고객관리 컨트롤러
* @name : MemberMgmtController
* @date : 2022/10/11 9:14 PM
* @author : SeokJun Kang(swings134@gmail.com)
* @version : 1.0.0
************/
@RestController
@RequiredArgsConstructor
@RequestMapping("/member/v1")
public class MemberMgmtController {

    private final MemberService memberService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    // -----------------> test Only
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/greeting")
    public String hello() {
        return "hello";
    }

    /**
     * methodName : findAll
     * author : TAEIL KIM
     * description :
     *
     * @return list
     */
    @ApiOperation(value ="고객 전체 조회", notes ="조건에 상관 없이 가입되어있는 모두를 조회함")
    @GetMapping("/memberlist")
    public List<Member> findAll() {
        return memberService.findAll();
    }

    // 회원 가입
    @PostMapping("/save")
    @PreAuthorize("isAnonymous()")
    @ApiOperation(value =  "회원 가입 api", notes = "회원 가입 처리함")
    public Long save(@RequestBody MemberDTO memberDTO) {
        Long result = memberService.save(memberDTO);
        return result;
    }

    /**
     * methodName : saveManage
     * author : TAEIL KIM
     * description : 매니저 계정은 어드민계정만 가능하다.
     *
     * @return Long
     */
    @PostMapping("/savemanage")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "admin권한을 가진 계정으로 매니저권한을 가진 계정을 생성 api", notes = "admin권한을 가진 계정으로 매니저권한을 가진 계정을 생성함")
    public Long manageSave(@RequestBody MemberDTO memberDTO) {
        Long result = memberService.manageSave(memberDTO);
        return result;
    }


    @DeleteMapping("/deletemember")
    @ApiOperation(value =  "회원 탈퇴 api", notes = "회원 탈퇴 처리함")
    public int deleteMember(MemberDTO inDto) {
        int result = memberService.deleteMember(inDto);
        return result;
    }

    @GetMapping("/findbyid")
    @ApiOperation(value =  "pk로 해당 고객 정보조회 api", notes = "단건 회원 정보 조회")
    //ID로 회원정보 검색 (PK)
    public MemberDTO findById (@RequestParam Long id) {
        MemberDTO inDto = MemberDTO.builder()
                .memberNo(id)
                .build();
        MemberDTO result = memberService.findById(inDto);
        return result;
    }

    /**
     * methodName : updateMember
     * author : TAEIL KIM
     * description :
     *
     * @param memberDTO
     * @return qna dto
     */
    @ApiOperation(value = "회원정보 수정 api", notes = "회원 정보를 수정함")
    @PostMapping("/updatemember")
    public MemberDTO updateMember(@RequestBody MemberDTO memberDTO) {
        return memberService.updateMember(memberDTO);
    }

     //이름으로 회원정보 검색
    @GetMapping("/member/v1/findbyname")
    public MemberDTO findByName(@RequestParam String name) {
        MemberDTO inDto = MemberDTO.builder()
                .name(name)
                .build();

        MemberDTO result = memberService.findByName(inDto);

        return result;
    }

    // 회원 아이디로 회원 객체 검색
    @GetMapping("/findbymemberid")
    @PreAuthorize("isAnonymous()")
    public MemberDTO findByMemberId(@RequestParam String memberId) {
        MemberDTO inDTO = MemberDTO.builder()
                .memberId(memberId)
                .build();
        MemberDTO result = memberService.findByMemberId(inDTO);
        return result;
    }


    /**
     * methodName : chkdupliId
     * author : TAEIL KIM
     * description : 회원가입시 사용되는 아이디 중복확인 api
     *
     * @return int
     */
    @PreAuthorize("isAnonymous()")
    @GetMapping("/chkdupliId")
    @ApiOperation(value =  "회원가입시 사용되는 아이디 중복확인 api", notes = "아이디 중복을 체크함")
    public int chkDupliId(@RequestParam String memberId) {
        return memberService.chkDupliId(memberId);
    }

    // 회원 이름 update
    @PutMapping("/updatename")
    public MemberDTO updateName(@RequestParam String name) {
        MemberDTO inDto = MemberDTO.builder()
                .name(name)
                .build();

        MemberDTO out = memberService.updateName(inDto);
        return out;
    }
}//class
