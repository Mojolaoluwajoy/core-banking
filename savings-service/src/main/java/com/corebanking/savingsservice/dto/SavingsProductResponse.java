
package com.corebanking.savingsservice.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SavingsProductResponse {

    private Long id;
    private String name;
    private String shortName;
    private String description;
    private BigDecimal nominalAnnualInterestRate;
    private BigDecimal minimumOpeningBalance;
    private BigDecimal minimumBalance;
    private boolean enforceMinimumBalance;
    private LocalDateTime createdAt;
}