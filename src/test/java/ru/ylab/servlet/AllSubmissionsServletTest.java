package ru.ylab.servlet;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.controller.SubmissionController;
import ru.ylab.dto.request.AllSubmissionsRequestDTO;
import ru.ylab.dto.response.SubmissionDTO;
import ru.ylab.exception.UserNotAuthorizedException;
import ru.ylab.utils.JsonUtils;
import ru.ylab.utils.RequestValidator;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AllSubmissionsServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private SubmissionController submissionController;

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private ServletInputStream inputStream;

    @Mock
    private ServletOutputStream outputStream;

    private AllSubmissionsServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new AllSubmissionsServlet();
        servlet.setSubmissionController(submissionController);
        servlet.setRequestValidator(requestValidator);
    }

    @Test
    void testDoGet_whenValidRequest_returnStatusOK() throws Exception {
        AllSubmissionsRequestDTO requestDTO = new AllSubmissionsRequestDTO(1L);
        Collection<SubmissionDTO> responseDTO = new ArrayList<>();
        byte[] requestBody = JsonUtils.writeJsonAsBytes(requestDTO);
        BufferedReader reader = new BufferedReader(new StringReader(new String(requestBody)));

        when(request.getReader()).thenReturn(reader);
        when(request.getInputStream()).thenReturn(inputStream);
        when(submissionController.getAllSubmissionDTOs(any())).thenReturn(responseDTO);
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
