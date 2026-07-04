
package com.corebanking.savingsservice.dto;

import com.corebanking.savingsservice.enums.SavingsAccountStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SavingsAccountResponse {

    private Long id;
    private Long clientId;
    private String productName;
    private String accountNumber;
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private SavingsAccountStatus status;
    private LocalDateTime createdAt;
}