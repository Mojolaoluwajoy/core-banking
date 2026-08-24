package com.corebanking.fixeddepositservice.repository;

import com.corebanking.fixeddepositservice.entity.FixedDeposit;
import com.corebanking.fixeddepositservice.enums.FixedDepositStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedDepositRepository extends JpaRepository<FixedDeposit, Long> {

  List<FixedDeposit> findByClientId(Long clientId);

  List<FixedDeposit> findByStatus(FixedDepositStatus status);

  List<FixedDeposit> findByStatusAndMaturityDateLessThanEqual(
      FixedDepositStatus status, LocalDate date);
}
