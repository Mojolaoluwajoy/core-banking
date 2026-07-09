
package com.corebanking.ledgerservice;

import com.corebanking.ledgerservice.dto.LedgerEntryResponse;
import com.corebanking.ledgerservice.dto.PostLedgerEntryRequest;
import com.corebanking.ledgerservice.entity.GlAccount;
import com.corebanking.ledgerservice.enums.GlAccountType;
import com.corebanking.ledgerservice.entity.LedgerEntry;
import com.corebanking.ledgerservice.enums.TransactionEntryType;
import com.corebanking.ledgerservice.exception.GlAccountNotFoundException;
import com.corebanking.ledgerservice.repository.GlAccountRepository;
import com.corebanking.ledgerservice.repository.LedgerEntryRepository;
import com.corebanking.ledgerservice.service.LedgerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LedgerServiceTest {

    @Mock
    private LedgerEntryRepository ledgerEntryRepository;

    @Mock
    private GlAccountRepository glAccountRepository;

    @InjectMocks
    private LedgerService ledgerService;

    private GlAccount cashAccount;
    private GlAccount savingsControlAccount;
    private GlAccount fixedDepositAssetAccount;
    private LedgerEntry savedEntry;
    private PostLedgerEntryRequest depositRequest;
    private PostLedgerEntryRequest withdrawalRequest;
    private PostLedgerEntryRequest fdCreationRequest;

    @BeforeEach
    void setUp() {

        cashAccount = new GlAccount();
        cashAccount.setId(1L);
        cashAccount.setAccountCode("1001");
        cashAccount.setAccountName("Cash");
        cashAccount.setAccountType(GlAccountType.ASSET);

        savingsControlAccount = new GlAccount();
        savingsControlAccount.setId(2L);
        savingsControlAccount.setAccountCode("2001");
        savingsControlAccount.setAccountName("Savings Control");
        savingsControlAccount.setAccountType(GlAccountType.LIABILITY);

        fixedDepositAssetAccount = new GlAccount();
        fixedDepositAssetAccount.setId(3L);
        fixedDepositAssetAccount.setAccountCode("1002");
        fixedDepositAssetAccount.setAccountName("Fixed Deposit Asset");
        fixedDepositAssetAccount.setAccountType(GlAccountType.ASSET);

        savedEntry = new LedgerEntry();
        savedEntry.setId(1L);
        savedEntry.setDebitAccount(cashAccount);
        savedEntry.setCreditAccount(savingsControlAccount);
        savedEntry.setAmount(new BigDecimal("500.00"));
        savedEntry.setTransactionType(TransactionEntryType.SAVINGS_DEPOSIT);
        savedEntry.setTransactionReference("SAV001-123");

        depositRequest = new PostLedgerEntryRequest();
        depositRequest.setTransactionType(TransactionEntryType.SAVINGS_DEPOSIT);
        depositRequest.setAmount(new BigDecimal("500.00"));
        depositRequest.setTransactionReference("SAV001-123");
        depositRequest.setDescription("Savings deposit");

        withdrawalRequest = new PostLedgerEntryRequest();
        withdrawalRequest.setTransactionType(TransactionEntryType.SAVINGS_WITHDRAWAL);
        withdrawalRequest.setAmount(new BigDecimal("200.00"));
        withdrawalRequest.setTransactionReference("SAV001-456");
        withdrawalRequest.setDescription("Savings withdrawal");

        fdCreationRequest = new PostLedgerEntryRequest();
        fdCreationRequest.setTransactionType(TransactionEntryType.FIXED_DEPOSIT_CREATION);
        fdCreationRequest.setAmount(new BigDecimal("10000.00"));
        fdCreationRequest.setTransactionReference("FD-1-123");
        fdCreationRequest.setDescription("Fixed deposit creation");
    }


    @Test
    void postEntry_savingsDeposit_shouldDebitCashAndCreditSavingsControl() {
        when(glAccountRepository.findByAccountCode("1001")).thenReturn(Optional.of(cashAccount));
        when(glAccountRepository.findByAccountCode("2001")).thenReturn(Optional.of(savingsControlAccount));
        when(ledgerEntryRepository.save(any(LedgerEntry.class))).thenReturn(savedEntry);

        LedgerEntryResponse response = ledgerService.postEntry(depositRequest);

        assertNotNull(response);
        assertEquals("1001", response.getDebitAccountCode());
        assertEquals("2001", response.getCreditAccountCode());
        assertEquals(new BigDecimal("500.00"), response.getAmount());
        verify(ledgerEntryRepository, times(1)).save(any(LedgerEntry.class));
    }


    @Test
    void postEntry_savingsWithdrawal_shouldDebitSavingsControlAndCreditCash() {
        LedgerEntry withdrawalEntry = new LedgerEntry();
        withdrawalEntry.setId(2L);
        withdrawalEntry.setDebitAccount(savingsControlAccount);
        withdrawalEntry.setCreditAccount(cashAccount);
        withdrawalEntry.setAmount(new BigDecimal("200.00"));
        withdrawalEntry.setTransactionType(TransactionEntryType.SAVINGS_WITHDRAWAL);
        withdrawalEntry.setTransactionReference("SAV001-456");

        when(glAccountRepository.findByAccountCode("2001")).thenReturn(Optional.of(savingsControlAccount));
        when(glAccountRepository.findByAccountCode("1001")).thenReturn(Optional.of(cashAccount));
        when(ledgerEntryRepository.save(any(LedgerEntry.class))).thenReturn(withdrawalEntry);

        LedgerEntryResponse response = ledgerService.postEntry(withdrawalRequest);

        assertNotNull(response);
        assertEquals("2001", response.getDebitAccountCode());
        assertEquals("1001", response.getCreditAccountCode());
        assertEquals(new BigDecimal("200.00"), response.getAmount());
    }


    @Test
    void postEntry_fixedDepositCreation_shouldDebitFixedDepositAssetAndCreditCash() {
        LedgerEntry fdEntry = new LedgerEntry();
        fdEntry.setId(3L);
        fdEntry.setDebitAccount(fixedDepositAssetAccount);
        fdEntry.setCreditAccount(cashAccount);
        fdEntry.setAmount(new BigDecimal("10000.00"));
        fdEntry.setTransactionType(TransactionEntryType.FIXED_DEPOSIT_CREATION);
        fdEntry.setTransactionReference("FD-1-123");

        when(glAccountRepository.findByAccountCode("1002")).thenReturn(Optional.of(fixedDepositAssetAccount));
        when(glAccountRepository.findByAccountCode("1001")).thenReturn(Optional.of(cashAccount));
        when(ledgerEntryRepository.save(any(LedgerEntry.class))).thenReturn(fdEntry);

        LedgerEntryResponse response = ledgerService.postEntry(fdCreationRequest);

        assertNotNull(response);
        assertEquals("1002", response.getDebitAccountCode());
        assertEquals("1001", response.getCreditAccountCode());
        assertEquals(new BigDecimal("10000.00"), response.getAmount());
    }


    @Test
    void postEntry_withMissingGlAccount_shouldThrowGlAccountNotFoundException() {
        when(glAccountRepository.findByAccountCode("1001")).thenReturn(Optional.empty());

        assertThrows(GlAccountNotFoundException.class, () ->
                ledgerService.postEntry(depositRequest));

        verify(ledgerEntryRepository, never()).save(any(LedgerEntry.class));
    }
}