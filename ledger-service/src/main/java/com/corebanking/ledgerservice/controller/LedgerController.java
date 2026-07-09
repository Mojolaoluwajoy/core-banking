package com.corebanking.ledgerservice.controller;

import com.corebanking.ledgerservice.dto.ApiResponse;
import com.corebanking.ledgerservice.dto.LedgerEntryResponse;
import com.corebanking.ledgerservice.dto.PostLedgerEntryRequest;
import com.corebanking.ledgerservice.service.LedgerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/ledger")
@RequiredArgsConstructor
@Tag(name = "Ledger", description = "Endpoints for managing General Ledger entries")
public class LedgerController {

    private final LedgerService ledgerService;

    @Operation(
            summary = "Post a ledger entry",
            description = "Posts a double entry to the General Ledger. " +
                    "Called automatically by savings and fixed deposit services on every transaction."
    )
    @PostMapping("/entries")
    public ResponseEntity<ApiResponse<LedgerEntryResponse>> postEntry(
            @Valid @RequestBody PostLedgerEntryRequest request) {
        log.info("Request received to post ledger entry");
        LedgerEntryResponse response = ledgerService.postEntry(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ledger entry posted successfully", response));
    }

    @Operation(summary = "Get ledger entry by ID")
    @GetMapping("/entries/{id}")
    public ResponseEntity<ApiResponse<LedgerEntryResponse>> getEntryById(@PathVariable Long id) {
        LedgerEntryResponse response = ledgerService.getEntryById(id);
        return ResponseEntity.ok(ApiResponse.success("Ledger entry retrieved successfully", response));
    }

    @Operation(summary = "Get all ledger entries")
    @GetMapping("/entries")
    public ResponseEntity<ApiResponse<List<LedgerEntryResponse>>> getAllEntries() {
        List<LedgerEntryResponse> response = ledgerService.getAllEntries();
        return ResponseEntity.ok(ApiResponse.success("Ledger entries retrieved successfully", response));
    }

    @Operation(summary = "Get ledger entries by transaction reference")
    @GetMapping("/entries/reference/{reference}")
    public ResponseEntity<ApiResponse<List<LedgerEntryResponse>>> getEntriesByReference(
            @PathVariable String reference) {
        List<LedgerEntryResponse> response = ledgerService.getEntriesByReference(reference);
        return ResponseEntity.ok(ApiResponse.success("Ledger entries retrieved successfully", response));
    }
}