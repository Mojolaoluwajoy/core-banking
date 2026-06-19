
package com.corebanking.clientservice.controller;

import com.corebanking.clientservice.dto.ClientResponse;
import com.corebanking.clientservice.dto.CreateClientRequest;
import com.corebanking.clientservice.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;


    @PostMapping
    public ResponseEntity<ClientResponse> createClient(
            @Valid @RequestBody CreateClientRequest request) {
        log.info("Request received to create client");
        ClientResponse response = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        log.info("Request received to fetch client with ID: {}", id);
        return ResponseEntity.ok(clientService.getClientById(id));
    }


    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        log.info("Request received to fetch all clients");
        return ResponseEntity.ok(clientService.getAllClients());
    }


    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody CreateClientRequest request) {
        log.info("Request received to update client with ID: {}", id);
        return ResponseEntity.ok(clientService.updateClient(id, request));
    }
}