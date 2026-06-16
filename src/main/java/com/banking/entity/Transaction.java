package com.banking.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    private LocalDateTime transactionDate;

    @PrePersist
    public void prePersist() {
        transactionDate = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private Account receiverAccount;

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER, BILL_PAYMENT
    }

    public enum TransactionStatus {
        PENDING, SUCCESS, FAILED
    }
}