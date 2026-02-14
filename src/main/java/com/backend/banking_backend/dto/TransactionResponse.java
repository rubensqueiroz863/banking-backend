package com.backend.banking_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        String id,
        BigDecimal amount,
        String type,
        LocalDateTime createdAt
) {}
