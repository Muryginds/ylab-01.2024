package ru.ylab.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.controller.LoginController;
import ru.ylab.dto.request.UserAuthorizationRequestDTO;
import ru.ylab.dto.response.UserDTO;
import ru.ylab.enumerated.UserRole;
import ru.ylab.exception.UserNotAuthorizedException;
import ru.ylab.utils.JsonUtils;
import ru.ylab.utils.RequestValidator;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.mockito.Mockito.*;

class LoginServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private LoginController loginController;

    @Mock
    private ServletOutputStream outputStream;

    @Mock
    private RequestValidator requestValidator;


    private LoginServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new LoginServlet();
        servlet.setLoginController(loginController);
        servlet.setRequestValidator(requestValidator);
    }

    @Test
    void testDoPost_whenValidRequest_returnStatusOK() throws Exception {
        UserAuthorizationRequestDTO requestDTO = new UserAuthorizationRequestDTO("name", "password");
        UserDTO userDTO = new UserDTO(1L, "user", UserRole.USER);
        byte[] responseBody = JsonUtils.writeJsonAsBytes(userDTO);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(new String(JsonUtils.writeJsonAsBytes(requestDTO)))));
        when(loginController.authorize(requestDTO)).thenReturn(userDTO);
        when(response.getOutputStream()).thenReturn(outputStream);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getOutputStream();
        verify(outputStream).write(responseBody);
    }

    @Test
    void testDoPost_whenAuthorizationFails_returnStatusBadRequest() throws Exception {
        UserAuthorizationRequestDTO requestDTO = new UserAuthorizationRequestDTO("name", "password");

        doThrow(new UserNotAuthorizedException()).when(loginController).authorize(requestDTO);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(new String(JsonUtils.writeJsonAsBytes((requestDTO))))));
        when(response.getOutputStream()).thenReturn(outputStream);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response).setContentType("application/json");
        verify(response).getOutputStream();
        verify(outputStream).write(any(byte[].class));
    }
}
