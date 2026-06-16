# Banking Management System

A full-stack digital banking platform built with Java, Spring Boot, and MySQL.

## Features
- User registration with BCrypt password encryption
- Bank account management (Savings, Current, Fixed Deposit)
- Fund transfers between accounts
- Deposit and withdrawal transactions
- Loan application and management
- Spring Security authentication
- REST APIs

## Tech Stack
- Java 26
- Spring Boot 3.5.14
- Spring Security
- Spring Data JPA / Hibernate
- MySQL 8.0
- Lombok
- Maven

## API Endpoints

### Users
- POST /api/users/register
- GET /api/users/{id}
- PUT /api/users/{id}/kyc

### Accounts
- POST /api/accounts/create
- GET /api/accounts/{accountNumber}
- GET /api/accounts/{accountNumber}/balance
- GET /api/accounts/user/{userId}

### Transactions
- POST /api/transactions/deposit
- POST /api/transactions/withdraw
- POST /api/transactions/transfer
- GET /api/transactions/history/{accountNumber}

### Loans
- POST /api/loans/apply
- PUT /api/loans/{loanId}/approve
- PUT /api/loans/{loanId}/reject
- GET /api/loans/user/{userId}

## Setup Instructions
1. Install Java 17+, Maven, MySQL
2. Create database: `CREATE DATABASE banking_db;`
3. Update `application.properties` with your MySQL credentials
4. Run: `mvn spring-boot:run`
5. Access: `http://localhost:8080`