package com.corebanking.savingsservice.entity;

import com.corebanking.savingsservice.enums.SavingsAccountStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "savings_accounts")
public class SavingsAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

      @Column(nullable = false)
    private Long clientId;

       @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private SavingsProduct product;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;


    @Column(nullable = false)
    private BigDecimal availableBalance = BigDecimal.ZERO;

     @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SavingsAccountStatus status = SavingsAccountStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}