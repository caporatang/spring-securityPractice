package com.sp.fc.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * packageName : com.sp.fc.web.controller
 * fileName : HomeController
 * author : taeil
 * date : 2022/12/12
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/12/12        taeil                   최초생성
 */
@Controller
public class HomeController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/greeting")
    public String greeting() {
        return "hello";
    }

}