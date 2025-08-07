package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/assign")
    public String assignAccountant(@RequestParam Long userId, @RequestParam Long companyId) {
        return userService.assignAccountantToCompany(userId, companyId);
    }
}
