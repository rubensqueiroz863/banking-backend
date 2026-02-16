package com.backend.banking_backend.dto;

import java.math.BigDecimal;


public record AccountResponse (
  String id,
  String bankCode,
  String agency,
  String accountNumber,
  String checkDigit,
  BigDecimal balance
) {}
