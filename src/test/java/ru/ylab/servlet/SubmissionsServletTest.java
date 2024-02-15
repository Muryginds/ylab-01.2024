package ru.ylab.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.controller.ReadingsRecordingController;
import ru.ylab.controller.SubmissionController;
import ru.ylab.dto.request.NewReadingsSubmissionRequestDTO;
import ru.ylab.dto.request.SubmissionRequestDTO;
import ru.ylab.dto.response.MessageDTO;
import ru.ylab.dto.response.SubmissionDTO;
import ru.ylab.utils.JsonUtils;
import ru.ylab.utils.RequestValidator;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SubmissionsServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private SubmissionController submissionController;

    @Mock
    private ReadingsRecordingController readingsRecordingController;

    @Mock
    private ServletOutputStream outputStream;

    @Mock
    private RequestValidator requestValidator;

    private SubmissionsServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new SubmissionsServlet();
        servlet.setSubmissionController(submissionController);
        servlet.setReadingsRecordingController(readingsRecordingController);
        servlet.setRequestValidator(requestValidator);
    }

    @Test
    void testDoGet_whenValidRequest_returnStatusOK() throws Exception {
        SubmissionRequestDTO requestDTO = new SubmissionRequestDTO(LocalDate.now(), 1L);
        SubmissionDTO responseDTO = new SubmissionDTO(1L, null, "", null);
        byte[] responseBody = JsonUtils.writeJsonAsBytes(responseDTO);

        BufferedReader reader = new BufferedReader(new StringReader(new String(JsonUtils.writeJsonAsBytes(requestDTO))));
        when(request.getReader()).thenReturn(reader);
        when(response.getOutputStream()).thenReturn(outputStream);
        when(submissionController.getSubmissionDTO(requestDTO)).thenReturn(responseDTO);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getOutputStream();
        verify(outputStream).write(responseBody);
    }

    @Test
    void testDoPost_whenValidRequest_returnStatusOK() throws Exception {
        NewReadingsSubmissionRequestDTO requestDTO = new NewReadingsSubmissionRequestDTO(null);
        MessageDTO responseDTO = new MessageDTO("new readings saved");
        byte[] responseBody = JsonUtils.writeJsonAsBytes(responseDTO);

        BufferedReader reader = new BufferedReader(new StringReader(new String(JsonUtils.writeJsonAsBytes(requestDTO))));
        when(request.getReader()).thenReturn(reader);
        when(response.getOutputStream()).thenReturn(outputStream);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getOutputStream();
        verify(outputStream).write(responseBody);
    }
}
