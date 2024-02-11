package ru.ylab.servlet;

import com.fasterxml.jackson.core.JacksonException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import ru.ylab.controller.MeterTypeController;
import ru.ylab.dto.request.NewMeterTypeRequestDTO;
import ru.ylab.dto.response.MessageDTO;
import ru.ylab.exception.BaseMonitoringServiceException;
import ru.ylab.exception.UserNotAuthorizedException;
import ru.ylab.utils.ApplicationComponentsFactory;
import ru.ylab.utils.JsonUtils;
import ru.ylab.utils.ResponseUtils;
import ru.ylab.utils.RequestValidator;

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
            var requestDTO = JsonUtils.readJson(req.getReader(), NewMeterTypeRequestDTO.class);
            requestValidator.validateRequest(requestDTO);
            meterTypeController.save(requestDTO);
            var responseDTO = MessageDTO.builder().message("new meter type saved").build();
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
