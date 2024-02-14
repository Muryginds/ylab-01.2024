package io.ylab.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.ylab.controller.UserController;
import io.ylab.dto.response.UserDTO;
import io.ylab.enumerated.UserRole;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.utils.JsonUtils;

import static org.mockito.Mockito.*;

class CurrentUserServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private UserController userController;

    @Mock
    private ServletOutputStream outputStream;

    private CurrentUserServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new CurrentUserServlet();
        servlet.setUserController(userController);
    }

    @Test
    void testDoGet_whenValidRequest_returnStatusOK() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "name", UserRole.USER);
        byte[] responseBody = JsonUtils.writeJsonAsBytes(userDTO);

        when(userController.getCurrentUser()).thenReturn(userDTO);
        when(response.getOutputStream()).thenReturn(outputStream);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getOutputStream();
        verify(outputStream).write(responseBody);
    }

    @Test
    void testDoGet_whenUserNotAuthorized_returnStatusNotAuthorized() throws Exception {
        doThrow(new UserNotAuthorizedException()).when(userController).getCurrentUser();
        when(response.getOutputStream()).thenReturn(outputStream);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(response).getOutputStream();
        verify(outputStream).write(any(byte[].class));
    }
}
