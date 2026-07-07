
package com.corebanking.savingsservice.repository;

import com.corebanking.savingsservice.entity.SavingsTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SavingsTransactionRepository extends JpaRepository<SavingsTransaction, Long> {


    List<SavingsTransaction> findByAccountIdOrderByTransactionDateDesc(Long accountId);
}