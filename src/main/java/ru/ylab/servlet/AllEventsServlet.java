package ru.ylab.servlet;

import com.fasterxml.jackson.core.JacksonException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import ru.ylab.controller.AuditionEventController;
import ru.ylab.dto.request.AuditionEventsRequestDTO;
import ru.ylab.exception.BaseMonitoringServiceException;
import ru.ylab.exception.UserNotAuthorizedException;
import ru.ylab.utils.ApplicationComponentsFactory;
import ru.ylab.utils.JsonUtils;
import ru.ylab.utils.ResponseUtils;
import ru.ylab.utils.RequestValidator;

import java.io.IOException;

@WebServlet("/api/v1/events/all")
public class AllEventsServlet extends HttpServlet {
    @Setter
    private AuditionEventController auditionEventController;
    @Setter
    private RequestValidator requestValidator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var requestDTO = JsonUtils.readJson(req.getReader(), AuditionEventsRequestDTO.class);
            requestValidator.validateRequest(requestDTO);
            var eventDTO = auditionEventController.getEvents(requestDTO);
            var output = JsonUtils.writeJsonAsBytes(eventDTO);
            ResponseUtils.callOkResponse(resp, output);
        } catch (UserNotAuthorizedException ex) {
            ResponseUtils.callNotAuthorizedResponse(resp, ex);
        } catch (BaseMonitoringServiceException | JacksonException ex) {
            ResponseUtils.callErrorResponse(resp, ex);
        }
    }

    @Override
    public void init() throws ServletException {
        auditionEventController = ApplicationComponentsFactory.getAuditionEventController();
        requestValidator = ApplicationComponentsFactory.getRequestValidator();
    }
}
