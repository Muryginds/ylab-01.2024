package io.ylab.servlet;

import com.fasterxml.jackson.core.JacksonException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import io.ylab.controller.MeterTypeController;
import io.ylab.dto.request.NewMeterTypeRequestDto;
import io.ylab.dto.response.MessageDto;
import io.ylab.exception.BaseMonitoringServiceException;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.utils.ApplicationComponentsFactory;
import io.ylab.utils.JsonUtils;
import io.ylab.utils.ResponseUtils;
import io.ylab.utils.RequestValidator;

import java.io.IOException;

@WebServlet("/api/v1/meter-types")
public class MeterTypesServlet extends HttpServlet {
    @Setter
    private MeterTypeController meterTypeController;
    @Setter
    private RequestValidator requestValidator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var requestDTO = JsonUtils.readJson(req.getReader(), NewMeterTypeRequestDto.class);
            requestValidator.validateRequest(requestDTO);
            meterTypeController.save(requestDTO);
            var responseDTO = MessageDto.builder().message("new meter type saved").build();
            var output = JsonUtils.writeJsonAsBytes(responseDTO);
            ResponseUtils.callOkResponse(resp, output);
        } catch (UserNotAuthorizedException ex) {
            ResponseUtils.callNotAuthorizedResponse(resp, ex);
        } catch (BaseMonitoringServiceException | JacksonException ex) {
            ResponseUtils.callErrorResponse(resp, ex);
        }
    }

    @Override
    public void init() throws ServletException {
        meterTypeController = ApplicationComponentsFactory.getMeterTypeController();
        requestValidator = ApplicationComponentsFactory.getRequestValidator();
    }
}
