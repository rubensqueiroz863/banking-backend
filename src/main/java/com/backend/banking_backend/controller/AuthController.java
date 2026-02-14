package com.backend.banking_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;

import com.backend.banking_backend.model.User;
import com.backend.banking_backend.model.LoginRequest;
import com.backend.banking_backend.model.AuthResponse;
import com.backend.banking_backend.service.JwtService;
import com.backend.banking_backend.service.UserService;
import com.backend.banking_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Buscar conta do usuário
        var account = user.getAccounts().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Usuário não possui conta"));

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(
                new AuthResponse(token, user.getCpf(), user.getName(), account.getId())
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        try {
            User savedUser = userService.registerUser(user);
            // Buscar conta do usuário
        var account = user.getAccounts().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Usuário não possui conta"));

            String token = jwtService.generateToken(savedUser);

            return ResponseEntity.ok(
                    new AuthResponse(token, savedUser.getCpf(), savedUser.getName(), account.getId())
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        var user = userRepository.findByCpf(request.getCpf())
                .orElseThrow(() -> new RuntimeException("CPF ou senha inválidos."));

        if (!userService.passwordMatches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha inválida");
        }

        // Buscar conta do usuário
        var account = user.getAccounts().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Usuário não possui conta"));

        String token = jwtService.generateToken(user);

        // Retornar token + contaId
        return ResponseEntity.ok(
                new AuthResponse(token, user.getCpf(), user.getName(), account.getId())
        );
    }

}
