package com.corebanking.ledgerservice.util;

import com.corebanking.ledgerservice.dto.LedgerEntryResponse;
import com.corebanking.ledgerservice.entity.LedgerEntry;

public class LedgerEntryMapper {

    public static LedgerEntryResponse toResponse(LedgerEntry entry) {
        return LedgerEntryResponse.builder()
                .id(entry.getId())
                .transactionReference(entry.getTransactionReference())
                .transactionType(entry.getTransactionType())
                .debitAccountCode(entry.getDebitAccount().getAccountCode())
                .debitAccountName(entry.getDebitAccount().getAccountName())
                .creditAccountCode(entry.getCreditAccount().getAccountCode())
                .creditAccountName(entry.getCreditAccount().getAccountName())
                .amount(entry.getAmount())
                .description(entry.getDescription())
                .entryDate(entry.getEntryDate())
                .build();
    }

    private LedgerEntryMapper() {}
}