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
import ru.ylab.controller.MeterTypeController;
import ru.ylab.dto.request.NewMeterTypeRequestDTO;
import ru.ylab.dto.response.MessageDTO;
import ru.ylab.exception.BaseMonitoringServiceException;
import ru.ylab.exception.UserNotAuthorizedException;
import ru.ylab.servlet.MeterTypesServlet;
import ru.ylab.utils.JsonUtils;
import ru.ylab.utils.RequestValidator;
import ru.ylab.utils.ResponseUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.Mockito.*;

class MeterTypesServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private MeterTypeController meterTypeController;

    @Mock
    private ServletOutputStream outputStream;

    @Mock
    private ServletInputStream inputStream;

    @Mock
    private RequestValidator requestValidator;

    private MeterTypesServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new MeterTypesServlet();
        servlet.setMeterTypeController(meterTypeController);
        servlet.setRequestValidator(requestValidator);
    }

    @Test
    void testDoGet_whenSaveSuccessful_returnStatusOK() throws Exception {
        NewMeterTypeRequestDTO requestDTO = new NewMeterTypeRequestDTO("Electricity");
        MessageDTO responseDTO = new MessageDTO("new meter type saved");
        byte[] responseBody = JsonUtils.writeJsonAsBytes(responseDTO);

        doNothing().when(meterTypeController).save(requestDTO);
        byte[] requestBody = JsonUtils.writeJsonAsBytes(requestDTO);
        BufferedReader reader = new BufferedReader(new StringReader(new String(requestBody)));

        when(request.getReader()).thenReturn(reader);
        when(request.getInputStream()).thenReturn(inputStream);
        when(response.getOutputStream()).thenReturn(outputStream);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).getOutputStream();
        verify(outputStream).write(responseBody);
    }

    @Test
    void testDoGet_whenUserNotAuthorized_returnStatusUnauthorized() throws Exception {
        NewMeterTypeRequestDTO requestDTO = new NewMeterTypeRequestDTO("Electricity");
        doThrow(new UserNotAuthorizedException()).when(meterTypeController).save(requestDTO);
        byte[] requestBody = JsonUtils.writeJsonAsBytes(requestDTO);
        BufferedReader reader = new BufferedReader(new StringReader(new String(requestBody)));

        when(request.getReader()).thenReturn(reader);
        when(request.getInputStream()).thenReturn(inputStream);
        when(response.getOutputStream()).thenReturn(outputStream);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(response).getOutputStream();
        verify(outputStream).write(any(byte[].class));
    }
}
