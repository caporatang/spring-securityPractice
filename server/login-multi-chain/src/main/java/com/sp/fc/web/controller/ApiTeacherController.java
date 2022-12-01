package com.sp.fc.web.controller;

import com.sp.fc.web.student.Student;
import com.sp.fc.web.student.StudentManager;
import com.sp.fc.web.teacher.Teacher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * packageName : com.sp.fc.web.controller
 * fileName : ApiTeacherController
 * author : taeil
 * date : 2022/11/30
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/11/30        taeil                   최초생성
 */
@RestController
@RequestMapping("/api/teacher")
public class ApiTeacherController {

//    @Autowired
//    StudentManager studentManager;

    private final StudentManager studentManager;

    public ApiTeacherController(StudentManager studentManager) {
        this.studentManager = studentManager;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/students")
    public List<Student> studentList(@AuthenticationPrincipal Teacher teacher) {
        List<Student> test = studentManager.myStudentList(teacher.getId());
        return test;
    }


}