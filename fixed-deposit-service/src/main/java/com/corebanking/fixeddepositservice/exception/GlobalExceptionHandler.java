package com.corebanking.fixeddepositservice.exception;

import com.corebanking.fixeddepositservice.dto.ApiResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(FixedDepositNotFoundException.class)
  public ResponseEntity<ApiResponse<Object>> handleNotFound(FixedDepositNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
  }

  @ExceptionHandler(PrematureWithdrawalException.class)
  public ResponseEntity<ApiResponse<Object>> handlePrematureWithdrawal(
      PrematureWithdrawalException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Object>> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.error("Validation failed: " + errors));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error("An unexpected error occurred: " + ex.getMessage()));
  }
}
