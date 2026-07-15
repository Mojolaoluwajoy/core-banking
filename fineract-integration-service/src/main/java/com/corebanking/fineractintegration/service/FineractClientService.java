
package com.corebanking.fineractintegration.service;

import com.corebanking.fineractintegration.client.FineractClient;
import com.corebanking.fineractintegration.dto.*;
import com.corebanking.fineractintegration.exception.FineractIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class FineractClientService {

    private final FineractClient fineractClient;


    public ClientResponse createClient(CreateClientRequest request) {
        log.info("Creating client in Fineract: {} {}", request.getFirstName(), request.getLastName());

        try {

            String activationDate = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

            FineractCreateClientRequest fineractRequest = FineractCreateClientRequest.builder()
                    .officeId(1)
                    .legalFormId(1)
                    .firstname(request.getFirstName())
                    .lastname(request.getLastName())
                    .active(true)
                    .activationDate(activationDate)
                    .dateFormat("dd MMMM yyyy")
                    .locale("en")
                    .mobileNo(request.getPhoneNumber())
                    .build();

            FineractClientResponse fineractResponse = fineractClient.createClient(fineractRequest);

            log.info("Client created in Fineract with ID: {}", fineractResponse.getClientId());


            return ClientResponse.builder()
                    .clientId(fineractResponse.getClientId())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .status("ACTIVE")
                    .build();

        } catch (Exception e) {
            log.error("Failed to create client in Fineract: {}", e.getMessage());
            throw new FineractIntegrationException(
                    "Failed to create client in Fineract: " + e.getMessage(), 500);
        }
    }


    public Object getClientById(Long clientId) {
        log.info("Fetching client {} from Fineract", clientId);

        try {
            return fineractClient.getClientById(clientId);
        } catch (Exception e) {
            log.error("Failed to fetch client from Fineract: {}", e.getMessage());
            throw new FineractIntegrationException(
                    "Client not found in Fineract with ID: " + clientId, 404);
        }
    }
}