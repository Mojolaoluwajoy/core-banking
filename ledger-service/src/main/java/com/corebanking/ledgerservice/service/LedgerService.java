package com.corebanking.ledgerservice.service;

import com.corebanking.ledgerservice.dto.LedgerEntryResponse;
import com.corebanking.ledgerservice.dto.PostLedgerEntryRequest;
import com.corebanking.ledgerservice.entity.*;
import com.corebanking.ledgerservice.exception.GlAccountNotFoundException;
import com.corebanking.ledgerservice.exception.LedgerEntryNotFoundException;
import com.corebanking.ledgerservice.repository.GlAccountRepository;
import com.corebanking.ledgerservice.repository.LedgerEntryRepository;
import com.corebanking.ledgerservice.util.LedgerEntryMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LedgerService {

  private final LedgerEntryRepository ledgerEntryRepository;
  private final GlAccountRepository glAccountRepository;

  @Transactional
  public LedgerEntryResponse postEntry(PostLedgerEntryRequest request) {
    log.info("Posting ledger entry for transaction type: {}", request.getTransactionType());

    String debitAccountCode;
    String creditAccountCode;

    switch (request.getTransactionType()) {
      case SAVINGS_DEPOSIT -> {
        debitAccountCode = "1001";
        creditAccountCode = "2001";
      }
      case SAVINGS_WITHDRAWAL -> {
        debitAccountCode = "2001";
        creditAccountCode = "1001";
      }
      case FIXED_DEPOSIT_CREATION -> {
        debitAccountCode = "1002";
        creditAccountCode = "1001";
      }
      case FIXED_DEPOSIT_MATURITY -> {
        debitAccountCode = "1001";
        creditAccountCode = "1002";
      }
      default ->
          throw new IllegalArgumentException(
              "Unknown transaction type: " + request.getTransactionType());
    }

    GlAccount debitAccount =
        glAccountRepository
            .findByAccountCode(debitAccountCode)
            .orElseThrow(
                () ->
                    new GlAccountNotFoundException(
                        "GL Account not found with code: " + debitAccountCode));

    GlAccount creditAccount =
        glAccountRepository
            .findByAccountCode(creditAccountCode)
            .orElseThrow(
                () ->
                    new GlAccountNotFoundException(
                        "GL Account not found with code: " + creditAccountCode));

    LedgerEntry entry = new LedgerEntry();
    entry.setTransactionReference(request.getTransactionReference());
    entry.setTransactionType(request.getTransactionType());
    entry.setDebitAccount(debitAccount);
    entry.setCreditAccount(creditAccount);
    entry.setAmount(request.getAmount());
    entry.setDescription(request.getDescription());

    LedgerEntry savedEntry = ledgerEntryRepository.save(entry);
    log.info("Ledger entry posted successfully with ID: {}", savedEntry.getId());

    return LedgerEntryMapper.toResponse(savedEntry);
  }

  public List<LedgerEntryResponse> getEntriesByReference(String reference) {
    return ledgerEntryRepository.findByTransactionReference(reference).stream()
        .map(LedgerEntryMapper::toResponse)
        .collect(Collectors.toList());
  }

  public List<LedgerEntryResponse> getAllEntries() {
    return ledgerEntryRepository.findAll().stream()
        .map(LedgerEntryMapper::toResponse)
        .collect(Collectors.toList());
  }

  public LedgerEntryResponse getEntryById(Long id) {
    LedgerEntry entry =
        ledgerEntryRepository
            .findById(id)
            .orElseThrow(
                () -> new LedgerEntryNotFoundException("Ledger entry not found with ID: " + id));
    return LedgerEntryMapper.toResponse(entry);
  }
}
