
package com.corebanking.fixeddepositservice.entity;

import com.corebanking.fixeddepositservice.enums.FixedDepositStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fixed_deposits")
public class FixedDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private Long clientId;


    @Column(nullable = false)
    private BigDecimal principalAmount;


    @Column(nullable = false)
    private BigDecimal interestRate;


    @Column(nullable = false)
    private Integer tenureInDays;


    @Column(nullable = false)
    private LocalDate depositDate;


    @Column(nullable = false)
    private LocalDate maturityDate;


    @Column(nullable = false)
    private BigDecimal maturityAmount;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FixedDepositStatus status = FixedDepositStatus.ACTIVE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}