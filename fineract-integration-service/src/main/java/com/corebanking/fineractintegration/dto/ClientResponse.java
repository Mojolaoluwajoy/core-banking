package com.corebanking.fineractintegration.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientResponse {
  private Long clientId;
  private String firstName;
  private String lastName;
  private String email;
  private String status;
}
