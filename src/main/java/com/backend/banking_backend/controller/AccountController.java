package com.backend.banking_backend.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import com.backend.banking_backend.dto.AccountResponse;
import com.backend.banking_backend.service.AccountService;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/balance/{accountId}")
    public AccountResponse getBalance(@PathVariable String accountId) {
        return accountService.getBalance(accountId);
    }
}
