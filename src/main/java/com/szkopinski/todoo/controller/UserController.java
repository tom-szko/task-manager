package com.szkopinski.todoo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/user")
    @ResponseBody
    public Principal getUser(Principal user) {
        return user;
    }
}
