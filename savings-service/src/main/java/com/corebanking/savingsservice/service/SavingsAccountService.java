package com.corebanking.savingsservice.service;

import com.corebanking.savingsservice.dto.OpenSavingsAccountRequest;
import com.corebanking.savingsservice.dto.SavingsAccountResponse;
import com.corebanking.savingsservice.dto.TransactionRequest;
import com.corebanking.savingsservice.dto.TransactionResponse;
import com.corebanking.savingsservice.entity.SavingsAccount;
import com.corebanking.savingsservice.enums.SavingsAccountStatus;
import com.corebanking.savingsservice.entity.SavingsProduct;
import com.corebanking.savingsservice.entity.SavingsTransaction;
import com.corebanking.savingsservice.enums.TransactionType;
import com.corebanking.savingsservice.exception.AccountNotFoundException;
import com.corebanking.savingsservice.exception.InsufficientBalanceException;
import com.corebanking.savingsservice.exception.ProductNotFoundException;
import com.corebanking.savingsservice.repository.SavingsAccountRepository;
import com.corebanking.savingsservice.repository.SavingsProductRepository;
import com.corebanking.savingsservice.repository.SavingsTransactionRepository;
import com.corebanking.savingsservice.util.LedgerClient;
import com.corebanking.savingsservice.util.SavingsAccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingsAccountService {

    private final SavingsAccountRepository savingsAccountRepository;
    private final SavingsProductRepository savingsProductRepository;
    private final SavingsTransactionRepository savingsTransactionRepository;
    private final LedgerClient ledgerClient;

    @Transactional
    public SavingsAccountResponse openAccount(OpenSavingsAccountRequest request) {
        log.info("Opening savings account for client ID: {}", request.getClientId());

        SavingsProduct product = savingsProductRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(
                        "Savings product not found with ID: " + request.getProductId()));

        if (request.getInitialDeposit().compareTo(product.getMinimumOpeningBalance()) < 0) {
            throw new InsufficientBalanceException(
                    "Initial deposit of " + request.getInitialDeposit() +
                            " is below the minimum opening balance of " + product.getMinimumOpeningBalance());
        }

        SavingsAccount account = new SavingsAccount();
        account.setClientId(request.getClientId());
        account.setProduct(product);
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(request.getInitialDeposit());
        account.setAvailableBalance(request.getInitialDeposit());
        account.setStatus(SavingsAccountStatus.ACTIVE);

        SavingsAccount savedAccount = savingsAccountRepository.save(account);
        log.info("Savings account opened successfully: {}", savedAccount.getAccountNumber());

        return SavingsAccountMapper.toResponse(savedAccount);
    }

    @Transactional
    public TransactionResponse deposit(Long accountId, TransactionRequest request) {
        log.info("Processing deposit of {} to account ID: {}", request.getAmount(), accountId);

        SavingsAccount account = savingsAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Savings account not found with ID: " + accountId));

        validateAccountIsActive(account);

        BigDecimal balanceBefore = account.getBalance();
        BigDecimal newBalance = balanceBefore.add(request.getAmount());

        account.setBalance(newBalance);
        account.setAvailableBalance(newBalance);
        savingsAccountRepository.save(account);

        SavingsTransaction transaction = new SavingsTransaction();
        transaction.setAccount(account);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(request.getAmount());
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(newBalance);
        savingsTransactionRepository.save(transaction);


        ledgerClient.postDepositEntry(request.getAmount(), savedAccount.getAccountNumber() + "-" + System.currentTimeMillis());
        log.info("Deposit successful. New balance: {}", newBalance);

        return TransactionResponse.builder()
                .accountId(accountId)
                .transactionType("DEPOSIT")
                .amount(request.getAmount())
                .balanceBefore(balanceBefore)
                .newBalance(newBalance)
                .transactionDate(LocalDateTime.now())
                .build();
    }

    @Transactional
    public TransactionResponse withdraw(Long accountId, TransactionRequest request) {
        log.info("Processing withdrawal of {} from account ID: {}", request.getAmount(), accountId);

        SavingsAccount account = savingsAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Savings account not found with ID: " + accountId));

        validateAccountIsActive(account);

        BigDecimal balanceBefore = account.getBalance();
        BigDecimal balanceAfterWithdrawal = balanceBefore.subtract(request.getAmount());

        if (account.getProduct().isEnforceMinimumBalance() &&
                balanceAfterWithdrawal.compareTo(account.getProduct().getMinimumBalance()) < 0) {
            throw new InsufficientBalanceException(
                    "Withdrawal of " + request.getAmount() +
                            " would breach the minimum balance of " +
                            account.getProduct().getMinimumBalance());
        }

        account.setBalance(balanceAfterWithdrawal);
        account.setAvailableBalance(balanceAfterWithdrawal);
        savingsAccountRepository.save(account);

        SavingsTransaction transaction = new SavingsTransaction();
        transaction.setAccount(account);
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setAmount(request.getAmount());
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfterWithdrawal);
        savingsTransactionRepository.save(transaction);


        ledgerClient.postWithdrawalEntry(request.getAmount(), account.getAccountNumber() + "-" + System.currentTimeMillis());
        log.info("Withdrawal successful. New balance: {}", balanceAfterWithdrawal);

        return TransactionResponse.builder()
                .accountId(accountId)
                .transactionType("WITHDRAWAL")
                .amount(request.getAmount())
                .balanceBefore(balanceBefore)
                .newBalance(balanceAfterWithdrawal)
                .transactionDate(LocalDateTime.now())
                .build();
    }

    public List<SavingsAccountResponse> getAccountsByClientId(Long clientId) {
        log.info("Fetching savings accounts for client ID: {}", clientId);

        return savingsAccountRepository.findByClientId(clientId)
                .stream()
                .map(SavingsAccountMapper::toResponse)
                .collect(Collectors.toList());
    }

    public SavingsAccountResponse getAccountById(Long accountId) {
        log.info("Fetching savings account with ID: {}", accountId);

        SavingsAccount account = savingsAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Savings account not found with ID: " + accountId));

        return SavingsAccountMapper.toResponse(account);
    }

    public List<SavingsTransaction> getTransactionHistory(Long accountId) {
        log.info("Fetching transaction history for account ID: {}", accountId);

        savingsAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Savings account not found with ID: " + accountId));

        return savingsTransactionRepository
                .findByAccountIdOrderByTransactionDateDesc(accountId);
    }

    private void validateAccountIsActive(SavingsAccount account) {
        if (account.getStatus() != SavingsAccountStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Account " + account.getAccountNumber() +
                            " is not active. Current status: " + account.getStatus());
        }
    }

    private String generateAccountNumber() {
        return "SAV" + System.currentTimeMillis();
    }
}