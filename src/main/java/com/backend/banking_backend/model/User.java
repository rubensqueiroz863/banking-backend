package com.backend.banking_backend.model;

import java.util.List;
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

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Account> accounts;
}
