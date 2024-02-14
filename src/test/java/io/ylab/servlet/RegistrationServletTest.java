package io.ylab.servlet;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.ylab.controller.LoginController;
import io.ylab.dto.request.UserRegistrationRequestDTO;
import io.ylab.dto.response.UserDTO;
import io.ylab.enumerated.UserRole;
import io.ylab.exception.BaseMonitoringServiceException;
import io.ylab.utils.JsonUtils;
import io.ylab.utils.RequestValidator;

import java.io.BufferedReader;
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
