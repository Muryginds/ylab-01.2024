package ru.ylab.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.controller.LoginController;
import ru.ylab.dto.request.UserRegistrationRequestDTO;
import ru.ylab.dto.response.UserDTO;
import ru.ylab.enumerated.UserRole;
import ru.ylab.exception.BaseMonitoringServiceException;
import ru.ylab.servlet.RegistrationServlet;
import ru.ylab.utils.JsonUtils;
import ru.ylab.utils.RequestValidator;
import ru.ylab.utils.ResponseUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.Mockito.*;

class RegistrationServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private LoginController loginController;

    @Mock
    private ServletOutputStream outputStream;
    @Mock
    private ServletInputStream inputStream;

    @Mock
    private RequestValidator requestValidator;

    private RegistrationServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new RegistrationServlet();
        servlet.setLoginController(loginController);
        servlet.setRequestValidator(requestValidator);
    }

    @Test
    void testDoPost_whenRegistrationSuccessful_returnStatusOK() throws Exception {
        UserRegistrationRequestDTO requestDTO = new UserRegistrationRequestDTO("username", "password");
        UserDTO responseDTO = new UserDTO(1L, "username", UserRole.USER);
        byte[] responseBody = JsonUtils.writeJsonAsBytes(responseDTO);
        byte[] requestBody = JsonUtils.writeJsonAsBytes(requestDTO);
        BufferedReader reader = new BufferedReader(new StringReader(new String(requestBody)));

        when(request.getReader()).thenReturn(reader);
        when(request.getInputStream()).thenReturn(inputStream);

        doReturn(outputStream).when(response).getOutputStream();
        when(loginController.register(requestDTO)).thenReturn(responseDTO);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getOutputStream();
        verify(outputStream).write(responseBody);
    }

    @Test
    void testDoPost_whenErrorOccurs_returnStatusBadRequest() throws Exception {
        UserRegistrationRequestDTO requestDTO = new UserRegistrationRequestDTO("username", "password");
        byte[] requestBody = JsonUtils.writeJsonAsBytes(requestDTO);
        BufferedReader reader = new BufferedReader(new StringReader(new String(requestBody)));

        when(request.getReader()).thenReturn(reader);
        when(request.getInputStream()).thenReturn(inputStream);

        doThrow(new BaseMonitoringServiceException("")).when(loginController).register(requestDTO);
        when(response.getOutputStream()).thenReturn(outputStream);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response).setContentType("application/json");
        verify(response).getOutputStream();
        verify(outputStream).write(any(byte[].class));
    }
}
