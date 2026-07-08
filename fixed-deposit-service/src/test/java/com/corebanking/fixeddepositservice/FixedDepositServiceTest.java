
package com.corebanking.fixeddepositservice;

import com.corebanking.fixeddepositservice.dto.CreateFixedDepositRequest;
import com.corebanking.fixeddepositservice.dto.FixedDepositResponse;
import com.corebanking.fixeddepositservice.entity.FixedDeposit;
import com.corebanking.fixeddepositservice.enums.FixedDepositStatus;
import com.corebanking.fixeddepositservice.exception.FixedDepositNotFoundException;
import com.corebanking.fixeddepositservice.exception.PrematureWithdrawalException;
import com.corebanking.fixeddepositservice.repository.FixedDepositRepository;
import com.corebanking.fixeddepositservice.service.FixedDepositService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FixedDepositServiceTest {

    @Mock
    private FixedDepositRepository fixedDepositRepository;

    @InjectMocks
    private FixedDepositService fixedDepositService;

    private CreateFixedDepositRequest validRequest;
    private FixedDeposit savedDeposit;

    @BeforeEach
    void setUp() {
        validRequest = new CreateFixedDepositRequest();
        validRequest.setClientId(1L);
        validRequest.setPrincipalAmount(new BigDecimal("10000.00"));
        validRequest.setInterestRate(new BigDecimal("12.00"));
        validRequest.setTenureInDays(180);

        savedDeposit = new FixedDeposit();
        savedDeposit.setId(1L);
        savedDeposit.setClientId(1L);
        savedDeposit.setPrincipalAmount(new BigDecimal("10000.00"));
        savedDeposit.setInterestRate(new BigDecimal("12.00"));
        savedDeposit.setTenureInDays(180);
        savedDeposit.setMaturityDate(LocalDate.now().plusDays(180));
        savedDeposit.setMaturityAmount(new BigDecimal("10591.78"));
        savedDeposit.setStatus(FixedDepositStatus.ACTIVE);
    }


    @Test
    void createFixedDeposit_withValidRequest_shouldReturnFixedDepositResponse() {
        when(fixedDepositRepository.save(any(FixedDeposit.class))).thenReturn(savedDeposit);

        FixedDepositResponse response = fixedDepositService.createFixedDeposit(validRequest);

        assertNotNull(response);
        assertEquals(new BigDecimal("10000.00"), response.getPrincipalAmount());
        assertEquals(FixedDepositStatus.ACTIVE, response.getStatus());
        assertNotNull(response.getMaturityDate());
        assertNotNull(response.getMaturityAmount());
        verify(fixedDepositRepository, times(1)).save(any(FixedDeposit.class));
    }

    @Test
    void matureDeposit_beforeMaturityDate_shouldThrowException() {
        savedDeposit.setMaturityDate(LocalDate.now().plusDays(90));
        when(fixedDepositRepository.findById(1L)).thenReturn(Optional.of(savedDeposit));

        assertThrows(PrematureWithdrawalException.class, () ->
                fixedDepositService.matureDeposit(1L));
    }


    @Test
    void getDepositById_withInvalidId_shouldThrowException() {
        when(fixedDepositRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(FixedDepositNotFoundException.class, () ->
                fixedDepositService.getDepositById(99L));
    }


    @Test
    void matureDeposit_onMaturityDate_shouldReturnMaturedResponse() {
        savedDeposit.setMaturityDate(LocalDate.now().minusDays(1));
        savedDeposit.setStatus(FixedDepositStatus.ACTIVE);

        FixedDeposit maturedDeposit = new FixedDeposit();
        maturedDeposit.setId(1L);
        maturedDeposit.setClientId(1L);
        maturedDeposit.setPrincipalAmount(new BigDecimal("10000.00"));
        maturedDeposit.setInterestRate(new BigDecimal("12.00"));
        maturedDeposit.setTenureInDays(180);
        maturedDeposit.setMaturityDate(LocalDate.now().minusDays(1));
        maturedDeposit.setMaturityAmount(new BigDecimal("10591.78"));
        maturedDeposit.setStatus(FixedDepositStatus.MATURED);

        when(fixedDepositRepository.findById(1L)).thenReturn(Optional.of(savedDeposit));
        when(fixedDepositRepository.save(any(FixedDeposit.class))).thenReturn(maturedDeposit);

        FixedDepositResponse response = fixedDepositService.matureDeposit(1L);

        assertNotNull(response);
        assertEquals(FixedDepositStatus.MATURED, response.getStatus());
    }
}