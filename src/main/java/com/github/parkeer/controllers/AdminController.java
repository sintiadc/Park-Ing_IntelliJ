package com.github.parkeer.controllers;

import com.github.parkeer.services.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping(value = "/test", produces = "text/plain")
    @ResponseBody
    public String test() {
        return adminService.test();
    }
}
