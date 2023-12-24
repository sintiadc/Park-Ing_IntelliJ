package com.github.parkeer.controllers;

import com.github.parkeer.enums.AccountRoleType;
import com.github.parkeer.services.EmployeeService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;

    @GetMapping(value = "/test", produces = "text/plain")
    @ResponseBody
    public String test() {
        return employeeService.test();
    }


}
