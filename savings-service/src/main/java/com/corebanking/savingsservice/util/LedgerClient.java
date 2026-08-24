package com.corebanking.savingsservice.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class LedgerClient {

  private final RestTemplate restTemplate;

  @Value("${ledger.service.url:http://localhost:8084}")
  private String ledgerServiceUrl;

  public void postDepositEntry(BigDecimal amount, String reference) {
    try {
      Map<String, Object> request = new HashMap<>();
      request.put("transactionType", "SAVINGS_DEPOSIT");
      request.put("amount", amount);
      request.put("transactionReference", reference);
      request.put("description", "Savings deposit - " + reference);

      restTemplate.postForObject(
          ledgerServiceUrl + "/api/v1/ledger/entries", request, Object.class);

      log.info("Ledger entry posted for deposit reference: {}", reference);
    } catch (Exception e) {
      log.error("Failed to post deposit ledger entry: {}", e.getMessage());
    }
  }

  public void postWithdrawalEntry(BigDecimal amount, String reference) {
    try {
      Map<String, Object> request = new HashMap<>();
      request.put("transactionType", "SAVINGS_WITHDRAWAL");
      request.put("amount", amount);
      request.put("transactionReference", reference);
      request.put("description", "Savings withdrawal - " + reference);

      restTemplate.postForObject(
          ledgerServiceUrl + "/api/v1/ledger/entries", request, Object.class);

      log.info("Ledger entry posted for withdrawal reference: {}", reference);
    } catch (Exception e) {
      log.error("Failed to post withdrawal ledger entry: {}", e.getMessage());
    }
  }
}
