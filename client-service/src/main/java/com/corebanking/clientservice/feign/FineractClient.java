package com.corebanking.clientservice.feign;

import com.corebanking.clientservice.dto.FineractClientResponse;
import com.corebanking.clientservice.dto.FineractCreateClientRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "fineract-client-service", url = "${fineract.base-url}")
public interface FineractClient {

  @PostMapping("/clients")
  FineractClientResponse createClient(@RequestBody FineractCreateClientRequest request);

  @GetMapping("/clients/{clientId}")
  Object getClientById(@PathVariable("clientId") Long clientId);
}
