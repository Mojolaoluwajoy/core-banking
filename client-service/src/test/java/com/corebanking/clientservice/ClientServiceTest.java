package com.corebanking.clientservice;


import com.corebanking.clientservice.dto.ClientResponse;
import com.corebanking.clientservice.dto.CreateClientRequest;
import com.corebanking.clientservice.entity.Client;
import com.corebanking.clientservice.exception.ClientNotFoundException;
import com.corebanking.clientservice.exception.DuplicateEmailException;
import com.corebanking.clientservice.repository.ClientRepository;
import com.corebanking.clientservice.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

//import static jdk.internal.classfile.impl.verifier.VerifierImpl.verify;
//import static jdk.jfr.internal.jfc.model.Constraint.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {


    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private CreateClientRequest validRequest;
    private Client savedClient;

    @BeforeEach
    void setUp() {
        validRequest = new CreateClientRequest();
        validRequest.setFirstName("Jane");
        validRequest.setLastName("Doe");
        validRequest.setEmail("janedoe@gmail.com");
        validRequest.setPhoneNumber("08142988584");
        validRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));

        savedClient = new Client();
        savedClient.setId(1L);
        savedClient.setFirstName("Jane");
        savedClient.setLastName("Doe");
        savedClient.setEmail("janedoe@gmail.com");
        savedClient.setPhoneNumber("08142988584");
        savedClient.setDateOfBirth(LocalDate.of(1990, 1, 1));
    }
        @Test
        void createClient_withValidRequest_shouldReturnClientResponse(){
        when(clientRepository.existsByEmail(validRequest.getEmail())).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        ClientResponse response=clientService.createClient(validRequest);

        assertNotNull(response);
        assertEquals("Jane",response.getFirstName());
        assertEquals("Doe",response.getLastName());
        assertEquals("janedoe@gmail.com",response.getEmail());
        verify(clientRepository,times(1)).save(any(Client.class));
        }

    @Test
    void createClient_withDuplicateEmail_shouldThrowException() {
        when(clientRepository.existsByEmail(validRequest.getEmail())).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> {
            clientService.createClient(validRequest);
        });

        verify(clientRepository, never()).save(any(Client.class));
    }

     @Test
    void getClientById_withInvalidId_shouldThrowException() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> {
            clientService.getClientById(99L);
        });
    }
}
