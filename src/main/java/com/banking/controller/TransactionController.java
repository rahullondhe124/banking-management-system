package com.banking.controller;

import com.banking.entity.Transaction;
import com.banking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Deposit
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestParam String accountNumber,
                                      @RequestParam BigDecimal amount) {
        try {
            Transaction t = transactionService.deposit(accountNumber, amount);
            return ResponseEntity.ok(t);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Withdraw
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestParam String accountNumber,
                                       @RequestParam BigDecimal amount) {
        try {
            Transaction t = transactionService.withdraw(accountNumber, amount);
            return ResponseEntity.ok(t);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Transfer
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestParam String senderAccountNumber,
                                       @RequestParam String receiverAccountNumber,
                                       @RequestParam BigDecimal amount) {
        try {
            Transaction t = transactionService.transfer(
                    senderAccountNumber, receiverAccountNumber, amount);
            return ResponseEntity.ok(t);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Transaction history
    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<?> getHistory(@PathVariable String accountNumber) {
        try {
            List<Transaction> history =
                    transactionService.getTransactionHistory(accountNumber);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}