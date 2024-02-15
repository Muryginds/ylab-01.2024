package io.ylab.servlet;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.ylab.controller.MeterTypeController;
import io.ylab.dto.request.NewMeterTypeRequestDto;
import io.ylab.dto.response.MessageDto;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.utils.JsonUtils;
import io.ylab.utils.RequestValidator;

import java.io.BufferedReader;
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
        NewMeterTypeRequestDto requestDTO = new NewMeterTypeRequestDto("Electricity");
        MessageDto responseDTO = new MessageDto("new meter type saved");
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
        NewMeterTypeRequestDto requestDTO = new NewMeterTypeRequestDto("Electricity");
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
