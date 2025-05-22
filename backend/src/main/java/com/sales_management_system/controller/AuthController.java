package com.sales_management_system.controller;

import com.sales_management_system.security.service.LoginRequestDTO;
import com.sales_management_system.security.service.RegisterRequestDTO;
import com.sales_management_system.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        var login = authService.login(body);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        var register = authService.register(body);
        return ResponseEntity.ok(register);
    }

}