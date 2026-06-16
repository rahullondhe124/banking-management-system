package com.banking.service;

import com.banking.entity.Account;
import com.banking.entity.User;
import com.banking.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    // Create new account
    public Account createAccount(Long userId, Account.AccountType accountType) {
        User user = userService.getUserById(userId);

        Account account = new Account();
        account.setUser(user);
        account.setAccountType(accountType);
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);

        return accountRepository.save(account);
    }

    // Get account by account number
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found!"));
    }

    // Get all accounts of a user
    public List<Account> getUserAccounts(Long userId) {
        User user = userService.getUserById(userId);
        return accountRepository.findByUser(user);
    }

    // Get balance
    public BigDecimal getBalance(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        return account.getBalance();
    }

    // Generate unique 10 digit account number
    private String generateAccountNumber() {
        Random random = new Random();
        String accountNumber;
        do {
            accountNumber = String.valueOf(1000000000L + 
                (long)(random.nextDouble() * 9000000000L));
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}