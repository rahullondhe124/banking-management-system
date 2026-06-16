package com.banking.service;

import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    // Deposit money
    @Transactional
    public Transaction deposit(String accountNumber, BigDecimal amount) {
        Account account = accountService.getAccountByNumber(accountNumber);

        // Add money to account
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        // Record transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setTransactionType(Transaction.TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setReceiverAccount(account);
        transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
        transaction.setDescription("Deposit of " + amount);

        return transactionRepository.save(transaction);
    }

    // Withdraw money
    @Transactional
    public Transaction withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountService.getAccountByNumber(accountNumber);

        // Check sufficient balance
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance!");
        }

        // Deduct money from account
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        // Record transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
        transaction.setAmount(amount);
        transaction.setSenderAccount(account);
        transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
        transaction.setDescription("Withdrawal of " + amount);

        return transactionRepository.save(transaction);
    }

    // Transfer money
    @Transactional
    public Transaction transfer(String senderAccountNumber,
                                String receiverAccountNumber,
                                BigDecimal amount) {
        Account sender = accountService.getAccountByNumber(senderAccountNumber);
        Account receiver = accountService.getAccountByNumber(receiverAccountNumber);

        // Check sufficient balance
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance!");
        }

        // Deduct from sender
        sender.setBalance(sender.getBalance().subtract(amount));
        accountRepository.save(sender);

        // Add to receiver
        receiver.setBalance(receiver.getBalance().add(amount));
        accountRepository.save(receiver);

        // Record transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setTransactionType(Transaction.TransactionType.TRANSFER);
        transaction.setAmount(amount);
        transaction.setSenderAccount(sender);
        transaction.setReceiverAccount(receiver);
        transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
        transaction.setDescription("Transfer of " + amount +
                " from " + senderAccountNumber +
                " to " + receiverAccountNumber);

        return transactionRepository.save(transaction);
    }

    // Get transaction history
    public List<Transaction> getTransactionHistory(String accountNumber) {
        Account account = accountService.getAccountByNumber(accountNumber);
        List<Transaction> sent = transactionRepository.findBySenderAccount(account);
        List<Transaction> received = transactionRepository.findByReceiverAccount(account);
        sent.addAll(received);
        return sent;
    }

    // Generate unique transaction ID
    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}