package com.backend.banking_backend.repository;

import com.backend.banking_backend.model.Transaction;
import com.backend.banking_backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    // Todas as transações de uma conta (entrada ou saída)
    List<Transaction> findByFromAccountOrToAccount(Account fromAccount, Account toAccount);

    // Apenas saídas (expenses)
    List<Transaction> findByFromAccount(Account fromAccount);

    // Apenas entradas (income)
    List<Transaction> findByToAccount(Account toAccount);

    // Por tipo específico
    List<Transaction> findByType(String type);

    // Por conta + tipo
    List<Transaction> findByFromAccountAndType(Account fromAccount, String type);
}
