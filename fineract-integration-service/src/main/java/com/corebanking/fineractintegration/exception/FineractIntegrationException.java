package com.corebanking.fineractintegration.exception;

public class FineractIntegrationException extends RuntimeException {
  private final int statusCode;

  public FineractIntegrationException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
