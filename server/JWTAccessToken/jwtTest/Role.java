package com.trading.day.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ROLE")
public class Role {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId; // 권한번호 0,1,2

    private int roleNumber;
    private String roleName; // admin, manager ,user

//    @OneToMany(mappedBy = "roleId")
//    @JsonIgnore
//    private List<UserRole> roles = new ArrayList<>();

}
