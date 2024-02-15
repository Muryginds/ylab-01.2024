package io.ylab.servlet;

import io.ylab.dto.response.SubmissionDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.ylab.controller.ReadingsRecordingController;
import io.ylab.controller.SubmissionController;
import io.ylab.dto.request.NewReadingsSubmissionRequestDto;
import io.ylab.dto.request.SubmissionRequestDto;
import io.ylab.dto.response.MessageDto;
import io.ylab.utils.JsonUtils;
import io.ylab.utils.RequestValidator;

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
        SubmissionRequestDto requestDTO = new SubmissionRequestDto(LocalDate.now(), 1L);
        SubmissionDto responseDTO = new SubmissionDto(1L, null, "", null);
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
        NewReadingsSubmissionRequestDto requestDTO = new NewReadingsSubmissionRequestDto(null);
        MessageDto responseDTO = new MessageDto("new readings saved");
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
