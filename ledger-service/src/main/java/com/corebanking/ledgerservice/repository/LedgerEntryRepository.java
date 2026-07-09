package com.corebanking.ledgerservice.repository;

import com.corebanking.ledgerservice.entity.LedgerEntry;
import com.corebanking.ledgerservice.enums.TransactionEntryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByTransactionReference(String transactionReference);
    List<LedgerEntry> findByTransactionTypeOrderByEntryDateDesc(TransactionEntryType transactionType);
}