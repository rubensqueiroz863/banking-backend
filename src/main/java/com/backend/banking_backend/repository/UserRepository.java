package com.backend.banking_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.banking_backend.model.User;

public interface UserRepository extends JpaRepository<User, String>  {
  Optional<User> findByCpf(String cpf);
}
