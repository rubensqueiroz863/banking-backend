package com.backend.banking_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.banking_backend.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByAccountNumber(String accountNumber);
}
