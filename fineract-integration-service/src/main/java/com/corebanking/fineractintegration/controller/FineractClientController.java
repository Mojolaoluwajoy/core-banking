
package com.corebanking.fineractintegration.controller;

import com.corebanking.fineractintegration.dto.ApiResponse;
import com.corebanking.fineractintegration.dto.ClientResponse;
import com.corebanking.fineractintegration.dto.CreateClientRequest;
import com.corebanking.fineractintegration.service.FineractClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/fineract/clients")
@RequiredArgsConstructor
@Tag(name = "Fineract Client Management",
        description = "Endpoints that integrate with Fineract for client management")
public class FineractClientController {

    private final FineractClientService fineractClientService;

    @Operation(
            summary = "Create a client in Fineract",
            description = "Creates a new client directly in the Fineract core banking system. " +
                    "Handles all Fineract-specific formatting internally."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<ClientResponse>> createClient(
            @Valid @RequestBody CreateClientRequest request) {
        log.info("Request received to create client in Fineract");
        ClientResponse response = fineractClientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Client created successfully in Fineract", response));
    }

    @Operation(
            summary = "Get a client from Fineract",
            description = "Fetches a client's details directly from the Fineract core banking system."
    )
    @GetMapping("/{clientId}")
    public ResponseEntity<ApiResponse<Object>> getClientById(@PathVariable Long clientId) {
        log.info("Request received to fetch client {} from Fineract", clientId);
        Object response = fineractClientService.getClientById(clientId);
        return ResponseEntity.ok(ApiResponse.success("Client retrieved from Fineract", response));
    }
}