package com.surpimi4.crud.controller;


import com.surpimi4.crud.service.UserServiceImpl;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/admin")

public class AdminController {

    private final UserServiceImpl userService;


    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUserList() {
        return "user";
    }


    @GetMapping("/add")
    public String addUserForm() {
        return "addUser";
    }

}
