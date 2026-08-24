package com.corebanking.fixeddepositservice.dto;

import com.corebanking.fixeddepositservice.enums.FixedDepositStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FixedDepositResponse {

  private Long id;
  private Long clientId;
  private BigDecimal principalAmount;
  private BigDecimal interestRate;
  private Integer tenureInDays;
  private LocalDate depositDate;
  private LocalDate maturityDate;
  private BigDecimal maturityAmount;
  private FixedDepositStatus status;
  private LocalDateTime createdAt;
}
