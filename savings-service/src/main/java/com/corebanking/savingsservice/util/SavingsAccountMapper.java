/*
 * SavingsAccountMapper
 *
 * Handles mapping between SavingsAccount entity and DTOs.
 * Keeps service methods clean by centralizing
 * all mapping logic in one place.
 *
 * Same pattern as SavingsProductMapper and ClientMapper
 * — consistent utility approach across all services.
 */
package com.corebanking.savingsservice.util;

import com.corebanking.savingsservice.dto.SavingsAccountResponse;
import com.corebanking.savingsservice.entity.SavingsAccount;

public class SavingsAccountMapper {


    public static SavingsAccountResponse toResponse(SavingsAccount account) {
        return SavingsAccountResponse.builder()
                .id(account.getId())
                .clientId(account.getClientId())
                .productName(account.getProduct().getName())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .availableBalance(account.getAvailableBalance())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .build();
    }

    private SavingsAccountMapper() {}
}