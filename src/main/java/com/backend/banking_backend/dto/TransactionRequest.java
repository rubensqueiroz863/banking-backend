package com.backend.banking_backend.dto;

import java.math.BigDecimal;

public record TransactionRequest(
        String fromAccountId,
        String toAccountId,
        BigDecimal amount,
        String type // DEPOSIT, WITHDRAW, TRANSFER
) {}
