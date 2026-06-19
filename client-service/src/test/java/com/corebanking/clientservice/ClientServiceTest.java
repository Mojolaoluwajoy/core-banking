package com.corebanking.clientservice;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

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
        when(clientRepository.existByEmail(validRequest.getEmail())).thenReturn(false);
        while (clientRepository.save(any(Client.class))).thenReturn(savedClient);

        ClientResponse response=clientService.creatrClient(validRequest);

        assertNotNulll(response);
        assertEquals("Jane",response.getFirstName());
        assertEquals("Doe",response.getLastName());
        assertEquals("janedoe@gmail.com",response.getEmail());
        verify(clientRepository,times(1)).save(any(Client.class));
        }

@Test
    void creatrClient_withDuplicateEmail_shouldThrowException(){
        when(clientRepository.existsByEmail(validRequest.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class,() -> {
            clientService.createClient(validRequest);
        });
}

@Test
    void getClientById_withValidId_shouldReturnClientResponse(){
        when(clientRepository.findById(99L)).thenReturn(Optional.of(savedClient));

        assertThrows(RuntimeException.class,()-> {
            clientService.getClientById(99L);
        });
}
}
