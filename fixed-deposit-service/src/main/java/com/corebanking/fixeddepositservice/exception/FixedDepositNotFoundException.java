package com.corebanking.fixeddepositservice.exception;

public class FixedDepositNotFoundException extends RuntimeException {
  public FixedDepositNotFoundException(String message) {
    super(message);
  }
}
