package com.example.fooddeliverymarketplace.exception;

import com.example.fooddeliverymarketplace.payload.ErrorDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        authException.printStackTrace();
        String errorPath = request.getRequestURI();
        String errorMessage = "Login first."; // authException.getMessage();
        Integer errorCode = 401; // Unauthorized
        response.setStatus(errorCode);
        ServletOutputStream outputStream = response.getOutputStream();
        objectMapper.writeValue(outputStream, new ErrorDTO(errorMessage,errorPath,errorCode, LocalDateTime.now()));
    }
}
