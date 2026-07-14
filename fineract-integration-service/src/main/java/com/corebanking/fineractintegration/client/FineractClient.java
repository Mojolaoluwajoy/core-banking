/*
 * FineractClient
 *
 * WHAT: A Feign interface defining all Fineract API
 * endpoints we want to call.
 *
 * WHY: Instead of writing HTTP call code manually,
 * we define an interface and Feign automatically
 * creates the implementation. We just call methods
 * like normal Java — Feign handles the HTTP.
 *
 * The @FeignClient annotation tells Spring:
 * - name: the name of this client
 * - url: where Fineract is running
 *
 * The FineractRequestInterceptor automatically adds
 * the required headers to every call made here.
 */
package com.corebanking.fineractintegration.client;

import com.corebanking.fineractintegration.dto.FineractClientResponse;
import com.corebanking.fineractintegration.dto.FineractCreateClientRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "fineract-client", url = "${fineract.base-url}")
public interface FineractClient {


    @PostMapping("/clients")
    FineractClientResponse createClient(@RequestBody FineractCreateClientRequest request);


    @GetMapping("/clients/{clientId}")
    Object getClientById(@PathVariable("clientId") Long clientId);
}