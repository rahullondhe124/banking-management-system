package com.banking.controller;

import com.banking.entity.Loan;
import com.banking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    // Apply for loan
    @PostMapping("/apply")
    public ResponseEntity<?> applyLoan(@RequestParam Long userId,
                                        @RequestParam String accountNumber,
                                        @RequestParam BigDecimal loanAmount,
                                        @RequestParam BigDecimal interestRate,
                                        @RequestParam int tenureMonths,
                                        @RequestParam Loan.LoanType loanType) {
        try {
            Loan loan = loanService.applyLoan(userId, accountNumber,
                    loanAmount, interestRate, tenureMonths, loanType);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Approve loan (admin)
    @PutMapping("/{loanId}/approve")
    public ResponseEntity<?> approveLoan(@PathVariable Long loanId) {
        try {
            Loan loan = loanService.approveLoan(loanId);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Reject loan (admin)
    @PutMapping("/{loanId}/reject")
    public ResponseEntity<?> rejectLoan(@PathVariable Long loanId) {
        try {
            Loan loan = loanService.rejectLoan(loanId);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get user loans
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserLoans(@PathVariable Long userId) {
        try {
            List<Loan> loans = loanService.getUserLoans(userId);
            return ResponseEntity.ok(loans);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}