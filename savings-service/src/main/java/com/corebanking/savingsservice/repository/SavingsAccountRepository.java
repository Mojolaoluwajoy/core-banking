
package com.corebanking.savingsservice.repository;

import com.corebanking.savingsservice.entity.SavingsAccount;
import com.corebanking.savingsservice.enums.SavingsAccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {

    List<SavingsAccount> findByClientId(Long clientId);

       Optional<SavingsAccount> findByAccountNumber(String accountNumber);

       List<SavingsAccount> findByClientIdAndStatus(Long clientId, SavingsAccountStatus status);
}