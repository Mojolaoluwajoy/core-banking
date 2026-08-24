/*
 * FineractClientResponse
 *
 * WHAT: What Fineract returns after creating a client.
 * WHY: We need to capture Fineract's response
 * to extract the clientId and return it to our caller.
 */
package com.corebanking.fineractintegration.dto;

import lombok.Data;

@Data
public class FineractClientResponse {
  private Long clientId;
  private Long officeId;
  private Long resourceId;
}
