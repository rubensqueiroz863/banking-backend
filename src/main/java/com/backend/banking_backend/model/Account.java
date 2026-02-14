package com.backend.banking_backend.model;

import java.math.BigDecimal;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Accounts")
public class Account {

    @Id
    @Column
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String bankCode; // ex: 001

    @Column(nullable = false)
    private String agency; // ex: 0001

    @Column(nullable = false)
    private String accountNumber; // ex: 12345678

    @Column(nullable = false)
    private String checkDigit; // ex: 9

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
