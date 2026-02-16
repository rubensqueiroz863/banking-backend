package com.backend.banking_backend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.backend.banking_backend.dto.AccountResponse;
import com.backend.banking_backend.model.Account;
import com.backend.banking_backend.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountResponse getBalance(String accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        return mapToResponse(account);
    }


    private AccountResponse mapToResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getBankCode(),
                account.getAgency(),
                account.getAccountNumber(),
                account.getCheckDigit(),
                account.getBalance()
        );
    }
}
