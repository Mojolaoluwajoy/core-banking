
package com.corebanking.clientservice.dto;

import com.corebanking.clientservice.entity.ClientStatus;
import lombok.Data;
import lombok.Builder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ClientResponse {

     private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

      private ClientStatus status;
    private LocalDateTime createdAt;
}