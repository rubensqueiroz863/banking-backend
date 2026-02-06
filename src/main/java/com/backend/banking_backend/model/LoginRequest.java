package com.backend.banking_backend.model;

import lombok.Data;

@Data
public class LoginRequest {
  private String cpf;
  private String password;
}
