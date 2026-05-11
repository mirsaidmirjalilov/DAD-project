package com.example.fooddeliverymarketplace.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ErrorDTO(
        String errorMessage,
        String errorPath,
        Integer errorCode,
        LocalDateTime timestamp
) {
}
