package io.ylab.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.utils.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setStatus(403);
        response.getWriter().write(
                mapper.writeValueAsString(
                        ResponseUtils.responseWithMessage(
                                "Access denied: %s".formatted(authException.getMessage()))
                )
        );
    }
}
