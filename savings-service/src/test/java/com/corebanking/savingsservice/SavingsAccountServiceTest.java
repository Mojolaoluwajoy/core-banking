package com.corebanking.savingsservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.corebanking.savingsservice.dto.OpenSavingsAccountRequest;
import com.corebanking.savingsservice.dto.SavingsAccountResponse;
import com.corebanking.savingsservice.dto.TransactionRequest;
import com.corebanking.savingsservice.dto.TransactionResponse;
import com.corebanking.savingsservice.entity.SavingsAccount;
import com.corebanking.savingsservice.entity.SavingsProduct;
import com.corebanking.savingsservice.enums.SavingsAccountStatus;
import com.corebanking.savingsservice.exception.AccountNotFoundException;
import com.corebanking.savingsservice.exception.InsufficientBalanceException;
import com.corebanking.savingsservice.exception.ProductNotFoundException;
import com.corebanking.savingsservice.repository.SavingsAccountRepository;
import com.corebanking.savingsservice.repository.SavingsProductRepository;
import com.corebanking.savingsservice.service.SavingsAccountService;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SavingsAccountServiceTest {

  @Mock private SavingsAccountRepository savingsAccountRepository;

  @Mock private SavingsProductRepository savingsProductRepository;

  @InjectMocks private SavingsAccountService savingsAccountService;

  private SavingsProduct product;
  private SavingsAccount account;
  private OpenSavingsAccountRequest openRequest;

  @BeforeEach
  void setUp() {
    product = new SavingsProduct();
    product.setId(1L);
    product.setName("Regular Savings");
    product.setShortName("REGSAV");
    product.setNominalAnnualInterestRate(new BigDecimal("5.00"));
    product.setMinimumOpeningBalance(new BigDecimal("100.00"));
    product.setMinimumBalance(new BigDecimal("50.00"));
    product.setEnforceMinimumBalance(true);

    account = new SavingsAccount();
    account.setId(1L);
    account.setClientId(1L);
    account.setProduct(product);
    account.setAccountNumber("SAV000000001");
    account.setBalance(new BigDecimal("500.00"));
    account.setAvailableBalance(new BigDecimal("500.00"));
    account.setStatus(SavingsAccountStatus.ACTIVE);

    openRequest = new OpenSavingsAccountRequest();
    openRequest.setClientId(1L);
    openRequest.setProductId(1L);
    openRequest.setInitialDeposit(new BigDecimal("200.00"));
  }

  @Test
  void openAccount_withValidRequest_shouldReturnSavingsAccountResponse() {
    when(savingsProductRepository.findById(1L)).thenReturn(Optional.of(product));
    when(savingsAccountRepository.save(any(SavingsAccount.class))).thenReturn(account);

    SavingsAccountResponse response = savingsAccountService.openAccount(openRequest);

    assertNotNull(response);
    assertEquals("SAV000000001", response.getAccountNumber());
    assertEquals(SavingsAccountStatus.ACTIVE, response.getStatus());
    verify(savingsAccountRepository, times(1)).save(any(SavingsAccount.class));
  }

  @Test
  void openAccount_withInvalidProductId_shouldThrowException() {
    when(savingsProductRepository.findById(99L)).thenReturn(Optional.empty());
    openRequest.setProductId(99L);

    assertThrows(
        ProductNotFoundException.class, () -> savingsAccountService.openAccount(openRequest));

    verify(savingsAccountRepository, never()).save(any(SavingsAccount.class));
  }

  @Test
  void deposit_withValidAmount_shouldIncreaseBalance() {
    TransactionRequest request = new TransactionRequest();
    request.setAmount(new BigDecimal("200.00"));

    when(savingsAccountRepository.findById(1L)).thenReturn(Optional.of(account));
    when(savingsAccountRepository.save(any(SavingsAccount.class))).thenReturn(account);

    TransactionResponse response = savingsAccountService.deposit(1L, request);

    assertNotNull(response);
    assertEquals(new BigDecimal("700.00"), response.getNewBalance());
  }

  @Test
  void withdraw_belowMinimumBalance_shouldThrowException() {
    TransactionRequest request = new TransactionRequest();
    request.setAmount(new BigDecimal("460.00"));

    when(savingsAccountRepository.findById(1L)).thenReturn(Optional.of(account));

    assertThrows(
        InsufficientBalanceException.class, () -> savingsAccountService.withdraw(1L, request));

    verify(savingsAccountRepository, never()).save(any(SavingsAccount.class));
  }

  @Test
  void deposit_withInvalidAccountId_shouldThrowException() {
    TransactionRequest request = new TransactionRequest();
    request.setAmount(new BigDecimal("100.00"));

    when(savingsAccountRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(AccountNotFoundException.class, () -> savingsAccountService.deposit(99L, request));
  }
}
