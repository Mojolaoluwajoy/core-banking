
package com.corebanking.savingsservice.entity;

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
@Table(name = "savings_products")
public class SavingsProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String shortName;

    private String description;


    @Column(nullable = false)
    private BigDecimal nominalAnnualInterestRate;

      @Column(nullable = false)
    private BigDecimal minimumOpeningBalance;

    @Column(nullable = false)
    private BigDecimal minimumBalance;

    @Column(nullable = false)
    private boolean enforceMinimumBalance;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}