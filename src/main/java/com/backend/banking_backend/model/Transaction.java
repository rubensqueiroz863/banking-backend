package com.backend.banking_backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Transactions")
public class Transaction {

    @Id
    @Column
    private String id = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String type; 
    // DEPOSIT, WITHDRAW, TRANSFER

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
