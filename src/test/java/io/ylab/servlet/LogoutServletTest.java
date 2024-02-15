package io.ylab.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.ylab.controller.LoginController;
import io.ylab.dto.response.MessageDto;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.utils.JsonUtils;

import static org.mockito.Mockito.*;

class LogoutServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private LoginController loginController;

    @Mock
    private ServletOutputStream outputStream;

    private LogoutServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new LogoutServlet();
        servlet.setLoginController(loginController);
    }

    @Test
    void testDoPost_whenLogoutSuccessful_returnStatusOK() throws Exception {
        MessageDto responseDTO = new MessageDto("user logged out");
        byte[] responseBody = JsonUtils.writeJsonAsBytes(responseDTO);

        doNothing().when(loginController).logout();
        when(response.getOutputStream()).thenReturn(outputStream);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getOutputStream();
        verify(outputStream).write(responseBody);
    }

    @Test
    void testDoPost_whenUserNotLoggedIn_returnStatusUnauthorized() throws Exception {
        doThrow(new UserNotAuthorizedException()).when(loginController).logout();
        when(response.getOutputStream()).thenReturn(outputStream);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(response).getOutputStream();
        verify(outputStream).write(any(byte[].class));
    }
}
