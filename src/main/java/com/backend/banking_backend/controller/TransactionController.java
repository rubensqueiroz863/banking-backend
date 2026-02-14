package com.backend.banking_backend.controller;

import com.backend.banking_backend.dto.TransactionRequest;
import com.backend.banking_backend.dto.TransactionResponse;
import com.backend.banking_backend.service.TransactionService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService transactionService;

    // 🔎 Todas as transações
    @GetMapping("/{accountId}")
    public List<TransactionResponse> getAllTransactions(
            @PathVariable String accountId) {
        return transactionService.getAllTransactions(accountId);
    }

    // 💰 Apenas entradas
    @GetMapping("/{accountId}/income")
    public List<TransactionResponse> getIncome(
            @PathVariable String accountId) {
        return transactionService.getIncome(accountId);
    }

    // 💸 Apenas saídas
    @GetMapping("/{accountId}/expenses")
    public List<TransactionResponse> getExpenses(
            @PathVariable String accountId) {
        return transactionService.getExpenses(accountId);
    }

    // ➕ Criar transação
    @PostMapping
    public TransactionResponse createTransaction(
            @RequestBody TransactionRequest request) {
        return transactionService.createTransaction(request);
    }
}
