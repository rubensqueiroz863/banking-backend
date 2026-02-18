package com.backend.banking_backend.service;

import com.backend.banking_backend.dto.TransactionResponse;
import com.backend.banking_backend.dto.TransactionRequest;
import com.backend.banking_backend.model.Account;
import com.backend.banking_backend.model.Transaction;
import com.backend.banking_backend.repository.TransactionRepository;
import com.backend.banking_backend.repository.AccountRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    // 🔎 Todas as transações
    public List<TransactionResponse> getAllTransactions(String accountId) {
        Account account = getAccount(accountId);
        return transactionRepository
                .findByFromAccountOrToAccount(account, account)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 💰 Apenas entradas
    public List<TransactionResponse> getIncome(String accountId) {
        Account account = getAccount(accountId);
        return transactionRepository
                .findByToAccount(account)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 💸 Apenas saídas
    public List<TransactionResponse> getExpenses(String accountId) {
        Account account = getAccount(accountId);
        return transactionRepository
                .findByFromAccount(account)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ➕ Criar transação
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {

        Account fromAccount = accountRepository.findById(request.fromAccountId())
                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"));

        Account toAccount = accountRepository.findById(request.toAccountId())
                .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"));

        // Validação de saldo para saídas
        if (request.type().equalsIgnoreCase("WITHDRAW") 
                || request.type().equalsIgnoreCase("TRANSFER")) {
            if (fromAccount.getBalance().compareTo(request.amount()) < 0) {
                throw new RuntimeException("Saldo insuficiente");
            }
            fromAccount.setBalance(fromAccount.getBalance().subtract(request.amount()));
        }

        // Crédito para entradas
        if (request.type().equalsIgnoreCase("DEPOSIT") 
                || request.type().equalsIgnoreCase("TRANSFER")) {
            toAccount.setBalance(toAccount.getBalance().add(request.amount()));
        }

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(request.amount());
        transaction.setType(request.type().toUpperCase());

        transactionRepository.save(transaction);

        return mapToResponse(transaction);
    }

    // 🔐 Método privado para buscar conta
    private Account getAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
    }

    // 🔄 Mapper Entity -> DTO
    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCreatedAt(),
                transaction.getFromAccount().getUser().getName(),
                transaction.getToAccount().getUser().getName(),
                transaction.getFromAccount().getId(),
                transaction.getToAccount().getId()
        );
    }
}
