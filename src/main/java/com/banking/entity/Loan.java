package com.banking.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal loanAmount;

    @Column(nullable = false)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private int tenureMonths;

    private BigDecimal monthlyEmi;

    private BigDecimal remainingAmount;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus;

    private LocalDateTime appliedAt;

    private LocalDateTime approvedAt;

    @PrePersist
    public void prePersist() {
        appliedAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public enum LoanType {
        HOME, CAR, PERSONAL, EDUCATION
    }

    public enum LoanStatus {
        PENDING, APPROVED, REJECTED, CLOSED
    }
}