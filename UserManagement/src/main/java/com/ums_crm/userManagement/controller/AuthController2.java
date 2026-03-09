package com.ums_crm.userManagement.controller;

import com.ums_crm.userManagement.dto.LoginRequest;
import com.ums_crm.userManagement.dto.LoginResponse;
import com.ums_crm.userManagement.dto.RegisterRequest;
import com.ums_crm.userManagement.entity.User;
import com.ums_crm.userManagement.service.AuthServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController2 {

    @Autowired
    private AuthServiceInterface authService;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // raw password only
        return authService.registerUser(user);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        LoginResponse token = authService.login(request.getUsername(), request.getPassword());
        System.out.println("token===>"+token);
        return token;
    }
}
