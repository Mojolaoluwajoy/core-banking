
package com.corebanking.clientservice.service;

import com.corebanking.clientservice.dto.ClientResponse;
import com.corebanking.clientservice.dto.CreateClientRequest;
import com.corebanking.clientservice.entity.Client;
import com.corebanking.clientservice.repository.ClientRepository;
import com.corebanking.clientservice.util.ClientMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientResponse createClient(CreateClientRequest request) {
        log.info("Creating client with email: {}", request.getEmail());

        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Client with email " + request.getEmail() + " already exists");
        }

        Client savedClient = clientRepository.save(ClientMapper.toEntity(request));
        log.info("Client created successfully with ID: {}", savedClient.getId());

        return ClientMapper.toResponse(savedClient);
    }

    public ClientResponse getClientById(Long id) {
        log.info("Fetching client with ID: {}", id);

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + id));

        return ClientMapper.toResponse(client);
    }

    public List<ClientResponse> getAllClients() {
        log.info("Fetching all clients");

        return clientRepository.findAll()
                .stream()
                .map(ClientMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ClientResponse updateClient(Long id, CreateClientRequest request) {
        log.info("Updating client with ID: {}", id);

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + id));

        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setDateOfBirth(request.getDateOfBirth());

        return ClientMapper.toResponse(clientRepository.save(client));
    }
}