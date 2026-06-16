package com.banking.service;

import com.banking.entity.Account;
import com.banking.entity.Loan;
import com.banking.entity.User;
import com.banking.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    // Apply for loan
    public Loan applyLoan(Long userId, String accountNumber,
                          BigDecimal loanAmount, BigDecimal interestRate,
                          int tenureMonths, Loan.LoanType loanType) {

        User user = userService.getUserById(userId);
        Account account = accountService.getAccountByNumber(accountNumber);

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setAccount(account);
        loan.setLoanAmount(loanAmount);
        loan.setInterestRate(interestRate);
        loan.setTenureMonths(tenureMonths);
        loan.setLoanType(loanType);
        loan.setLoanStatus(Loan.LoanStatus.PENDING);
        loan.setRemainingAmount(loanAmount);

        // Calculate EMI
        loan.setMonthlyEmi(calculateEmi(loanAmount, interestRate, tenureMonths));

        return loanRepository.save(loan);
    }

    // Approve loan
    public Loan approveLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found!"));

        loan.setLoanStatus(Loan.LoanStatus.APPROVED);
        loan.setApprovedAt(LocalDateTime.now());

        return loanRepository.save(loan);
    }

    // Reject loan
    public Loan rejectLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found!"));
        loan.setLoanStatus(Loan.LoanStatus.REJECTED);
        return loanRepository.save(loan);
    }

    // Get loans by user
    public List<Loan> getUserLoans(Long userId) {
        User user = userService.getUserById(userId);
        return loanRepository.findByUser(user);
    }

    // Get loans by status (for admin)
    public List<Loan> getLoansByStatus(Loan.LoanStatus status) {
        return loanRepository.findByLoanStatus(status);
    }

    // EMI Calculator
    // Formula: EMI = P × R × (1+R)^N / ((1+R)^N - 1)
    private BigDecimal calculateEmi(BigDecimal principal,
                                     BigDecimal annualRate,
                                     int tenureMonths) {
        BigDecimal monthlyRate = annualRate
                .divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);

        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRPowN = onePlusR.pow(tenureMonths);

        BigDecimal emi = principal
                .multiply(monthlyRate)
                .multiply(onePlusRPowN)
                .divide(onePlusRPowN.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);

        return emi;
    }
}