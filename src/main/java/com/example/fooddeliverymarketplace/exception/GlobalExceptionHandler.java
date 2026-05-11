package com.example.fooddeliverymarketplace.exception;

import com.example.fooddeliverymarketplace.payload.BaseResponse;
import com.example.fooddeliverymarketplace.payload.ErrorDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public final ResponseEntity<BaseResponse> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        ErrorDTO error = getErrorDTO(ex, HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(BaseResponse.error(error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public final ResponseEntity<BaseResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        ErrorDTO error = getErrorDTO(ex, HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(error));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<BaseResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorDTO error = getErrorDTO(ex, HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(error));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<BaseResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ErrorDTO error = getErrorDTO(ex, HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(error));
    }

    private static ErrorDTO getErrorDTO(Exception ex, Integer errorCode) {
        return ErrorDTO.builder()
                .errorMessage(ex.getMessage())
                .errorCode(errorCode)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
