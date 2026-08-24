package com.corebanking.fixeddepositservice.util;

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

  public void postFixedDepositCreationEntry(BigDecimal amount, String reference) {
    try {
      Map<String, Object> request = new HashMap<>();
      request.put("transactionType", "FIXED_DEPOSIT_CREATION");
      request.put("amount", amount);
      request.put("transactionReference", reference);
      request.put("description", "Fixed deposit creation - " + reference);

      restTemplate.postForObject(
          ledgerServiceUrl + "/api/v1/ledger/entries", request, Object.class);

      log.info("Ledger entry posted for fixed deposit creation: {}", reference);
    } catch (Exception e) {
      log.error("Failed to post fixed deposit creation ledger entry: {}", e.getMessage());
    }
  }

  public void postFixedDepositMaturityEntry(BigDecimal amount, String reference) {
    try {
      Map<String, Object> request = new HashMap<>();
      request.put("transactionType", "FIXED_DEPOSIT_MATURITY");
      request.put("amount", amount);
      request.put("transactionReference", reference);
      request.put("description", "Fixed deposit maturity - " + reference);

      restTemplate.postForObject(
          ledgerServiceUrl + "/api/v1/ledger/entries", request, Object.class);

      log.info("Ledger entry posted for fixed deposit maturity: {}", reference);
    } catch (Exception e) {
      log.error("Failed to post fixed deposit maturity ledger entry: {}", e.getMessage());
    }
  }
}
