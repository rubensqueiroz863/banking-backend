package com.backend.banking_backend.model;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Users")

public class User {
  @Id
  @Column
  private String id = UUID.randomUUID().toString();

  @Column(unique = true, nullable = false)
  private String email;

  private String password;

  private String name;

  @Column(length = 11, nullable = false, unique = true)
  private String cpf;
}
