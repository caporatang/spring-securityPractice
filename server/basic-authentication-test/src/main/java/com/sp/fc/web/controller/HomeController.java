package com.sp.fc.web.controller;

import org.springframework.web.bind.annotation.*;

/**
 * packageName : controller
 * fileName : HomeController
 * author : taeil
 * date : 2022/11/29
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 2022/11/29        taeil                   최초생성
 */
@RestController
public class HomeController {

    @GetMapping("/greeting")
    public String greeting() {
        return "hello";
    }

    @PostMapping("/greeting")
    public String greeting(@RequestBody String name) {
        return "hello " + name;
    }


}