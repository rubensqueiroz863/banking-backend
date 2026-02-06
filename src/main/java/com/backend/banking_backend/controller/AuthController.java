package com.backend.banking_backend.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.backend.banking_backend.model.User;
import com.backend.banking_backend.model.LoginRequest;
import com.backend.banking_backend.model.AuthResponse;
import com.backend.banking_backend.repository.UserRepository;
import com.backend.banking_backend.service.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/auth")
public class AuthController {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  @GetMapping("/me")
  public ResponseEntity<?> me(Authentication authentication) {
    if (authentication == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    if (authentication.getPrincipal() instanceof User user) {
      String token = jwtService.generateToken(user);
      return ResponseEntity.ok(new AuthResponse(token, user.getCpf(), user.getName()));
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody User user) {
    if (userRepository.findByCpf(user.getCpf()).isPresent()) {
      return ResponseEntity.badRequest().body("Cpf inválido.");
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    return ResponseEntity.ok("Usuário cadastrado.");
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    var user = userRepository.findByCpf(request.getCpf())
              .orElseThrow(() -> new RuntimeException("CPF ou senha invalidos."));
    
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha inválida");
    }

    String token = jwtService.generateToken(user);
    return ResponseEntity.ok(new AuthResponse(token, user.getCpf(), user.getName()));
  }
}
