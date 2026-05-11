package com.example.fooddeliverymarketplace.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record BaseResponse<T>(T data, ErrorDTO error, boolean success) {
    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<>(data, null, true);
    }

    public static <T> BaseResponse<T> ok() {
        return new BaseResponse<>(null, null, true);
    }

    public static <T> BaseResponse<T> error(ErrorDTO error) {
        return new BaseResponse<>(null, error, false);
    }
}
