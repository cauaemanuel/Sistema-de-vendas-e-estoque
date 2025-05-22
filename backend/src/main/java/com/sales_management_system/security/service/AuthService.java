package com.sales_management_system.security.service;

import com.sales_management_system.Repository.UserRepository;
import com.sales_management_system.controller.dto.LoginRequestDTO;
import com.sales_management_system.controller.dto.RegisterRequestDTO;
import com.sales_management_system.controller.dto.ResponseDTO;
import com.sales_management_system.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    @Transactional
    public ResponseDTO login(LoginRequestDTO body){
        User user = this.repository
                .findByEmail(body.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));

        if (passwordEncoder.matches(body.password(), user.getPassword())){
            var token = tokenService.generateToken(user);
            return new ResponseDTO(user.getNome(), token);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseDTO register(RegisterRequestDTO body){
        var user = repository.findByEmail(body.email());
        if (user.isEmpty()){
            var newUser = new User();
            newUser.setEmail(body.email());
            newUser.setNome(body.name());
            newUser.setPassword(passwordEncoder.encode(body.password()));
            var token = tokenService.generateToken(newUser);
            repository.save(newUser);
            return new ResponseDTO(newUser.getNome(), token);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
