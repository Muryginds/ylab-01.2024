package io.ylab.servlet;

import io.ylab.dto.response.UserDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.ylab.controller.LoginController;
import io.ylab.dto.request.UserAuthorizationRequestDto;
import io.ylab.enumerated.UserRole;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.utils.JsonUtils;
import io.ylab.utils.RequestValidator;

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
        UserAuthorizationRequestDto requestDTO = new UserAuthorizationRequestDto("name", "password");
        UserDto userDTO = new UserDto(1L, "user", UserRole.USER);
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
        UserAuthorizationRequestDto requestDTO = new UserAuthorizationRequestDto("name", "password");

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
