package com.example.fooddeliverymarketplace.exception;

import com.example.fooddeliverymarketplace.exception.cart.CartEmptyExceprion;
import com.example.fooddeliverymarketplace.exception.cart.CartNotFoundException;
import com.example.fooddeliverymarketplace.exception.cart.UserCartNotFoundException;
import com.example.fooddeliverymarketplace.exception.menuitem.ItemInMenuNotFoundException;
import com.example.fooddeliverymarketplace.exception.restaurant.RestaurantNotFoundException;
import com.example.fooddeliverymarketplace.exception.user.UserAlreadyExistException;
import com.example.fooddeliverymarketplace.exception.user.UserNotFoundException;
import com.example.fooddeliverymarketplace.payload.BaseResponse;
import com.example.fooddeliverymarketplace.payload.ErrorDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DeliveryNotFoundException.class)
    public ResponseEntity<BaseResponse> handleDeliveryNotFoundException(DeliveryNotFoundException ex) {
        return getNotFoundBaseResponse(ex);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<BaseResponse> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        return getNotFoundBaseResponse(ex);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<BaseResponse> handleOrderNotFoundException(OrderNotFoundException ex) {
        return getNotFoundBaseResponse(ex);
    }

    @ExceptionHandler(CartEmptyExceprion.class)
    public ResponseEntity<BaseResponse> handleException(CartEmptyExceprion ex) {
        return getNotFoundBaseResponse(ex);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<BaseResponse> cartNotFoundException(CartNotFoundException ex) {
        return getNotFoundBaseResponse(ex);
    }

    @ExceptionHandler(UserCartNotFoundException.class)
    public ResponseEntity<BaseResponse> handleUserCartNotFoundException(UserCartNotFoundException ex) {
        ErrorDTO error = getErrorDTO(ex, HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(error));
    }

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
        return getNotFoundBaseResponse(ex);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<BaseResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return getNotFoundBaseResponse(ex);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<BaseResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return getNotFoundBaseResponse(ex);
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public final ResponseEntity<BaseResponse> handleRestaurantNotFoundException(RestaurantNotFoundException ex) {
        return getNotFoundBaseResponse(ex);
    }

    @ExceptionHandler(ItemInMenuNotFoundException.class)
    public final ResponseEntity<BaseResponse> handleItemInMenuNotFoundException(ItemInMenuNotFoundException ex) {
        return getNotFoundBaseResponse(ex);
    }

    private static ErrorDTO getErrorDTO(Exception ex, Integer errorCode) {
        return ErrorDTO.builder()
                .errorMessage(ex.getMessage())
                .errorCode(errorCode)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private static ResponseEntity<BaseResponse> getNotFoundBaseResponse(Exception ex) {
        ErrorDTO error = getErrorDTO(ex, HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(error));
    }
}
