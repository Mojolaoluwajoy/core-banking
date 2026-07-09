package com.corebanking.ledgerservice.dto;

import com.corebanking.ledgerservice.enums.TransactionEntryType;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class LedgerEntryResponse {

    private Long id;
    private String transactionReference;
    private TransactionEntryType transactionType;
    private String debitAccountCode;
    private String debitAccountName;
    private String creditAccountCode;
    private String creditAccountName;
    private BigDecimal amount;
    private String description;
    private LocalDateTime entryDate;
}