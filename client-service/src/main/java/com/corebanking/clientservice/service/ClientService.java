package com.corebanking.clientservice.service;
import com.corebanking.clientservice.dto.ClientResponse;
import com.corebanking.clientservice.dto.CreateClientRequest;
import com.corebanking.clientservice.dto.FineractClientResponse;
import com.corebanking.clientservice.dto.FineractCreateClientRequest;
import com.corebanking.clientservice.entity.Client;
import com.corebanking.clientservice.entity.ClientStatus;
import com.corebanking.clientservice.exception.ClientNotFoundException;
import com.corebanking.clientservice.exception.DuplicateEmailException;
import com.corebanking.clientservice.feign.FineractClient;
import com.corebanking.clientservice.repository.ClientRepository;
import com.corebanking.clientservice.util.ClientMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final FineractClient fineractClient;

    public ClientResponse createClient(CreateClientRequest request) {
        log.info("Creating client with email: {}", request.getEmail());

        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(
                    "Client with email " + request.getEmail() + " already exists");
        }


        Client client = ClientMapper.toEntity(request);
        client.setStatus(ClientStatus.PENDING);
        Client savedClient = clientRepository.save(client);
        log.info("Client saved to our database with ID: {}", savedClient.getId());


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


            savedClient.setFineractClientId(fineractResponse.getClientId());
            savedClient.setStatus(ClientStatus.ACTIVE);
            clientRepository.save(savedClient);

            log.info("Client also created in Fineract with ID: {}",
                    fineractResponse.getClientId());

        } catch (Exception e) {
            log.error("Failed to create client in Fineract: {}", e.getMessage());

        }

        return ClientMapper.toResponse(savedClient);
    }


    public ClientResponse getClientById(Long id) {
        log.info("Fetching client with ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(
                        "Client not found with ID: " + id));
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
                .orElseThrow(() -> new ClientNotFoundException(
                        "Client not found with ID: " + id));
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setDateOfBirth(request.getDateOfBirth());
        return ClientMapper.toResponse(clientRepository.save(client));
    }
}