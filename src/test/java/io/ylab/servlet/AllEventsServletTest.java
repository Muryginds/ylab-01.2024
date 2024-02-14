package io.ylab.servlet;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.ylab.controller.AuditionEventController;
import io.ylab.dto.request.AuditionEventsRequestDTO;
import io.ylab.dto.response.AuditionEventDTO;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.utils.JsonUtils;
import io.ylab.utils.RequestValidator;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AllEventsServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuditionEventController auditionEventController;

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private ServletInputStream inputStream;
    @Mock
    private ServletOutputStream outputStream;

    private AllEventsServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new AllEventsServlet();
        servlet.setAuditionEventController(auditionEventController);
        servlet.setRequestValidator(requestValidator);
    }

    @Test
    void testDoGet_whenValidRequest_returnStatusOK() throws Exception {

        AuditionEventsRequestDTO requestDTO = new AuditionEventsRequestDTO(1L);
        AuditionEventDTO responseDTO = AuditionEventDTO.builder().build();
        BufferedReader value = new BufferedReader(new StringReader(new String(JsonUtils.writeJsonAsBytes(requestDTO))));
        when(request.getReader()).thenReturn(value);
        when(request.getInputStream()).thenReturn(inputStream);
        when(auditionEventController.getEvents(any())).thenReturn(Collections.singleton(responseDTO));
        when(response.getOutputStream()).thenReturn(outputStream);

        doNothing().when(requestValidator).validateRequest(requestDTO);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getOutputStream();
        verify(response.getOutputStream()).write(any(byte[].class));
    }

    @Test
    void testDoGet_whenUserNotAuthorized_returnStatusNotAuthorized() throws Exception {
        when(request.getReader()).thenThrow(new UserNotAuthorizedException());
        when(response.getOutputStream()).thenReturn(outputStream);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(response).getOutputStream();
        verify(outputStream).write(any(byte[].class));
    }
}
