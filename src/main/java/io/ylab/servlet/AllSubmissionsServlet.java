package io.ylab.servlet;

import com.fasterxml.jackson.core.JacksonException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import io.ylab.controller.SubmissionController;
import io.ylab.dto.request.AllSubmissionsRequestDTO;
import io.ylab.exception.BaseMonitoringServiceException;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.utils.ApplicationComponentsFactory;
import io.ylab.utils.JsonUtils;
import io.ylab.utils.ResponseUtils;
import io.ylab.utils.RequestValidator;

import java.io.IOException;

@WebServlet("/api/v1/submissions/all")
public class AllSubmissionsServlet extends HttpServlet {
    @Setter
    private SubmissionController submissionController;
    @Setter
    private RequestValidator requestValidator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var requestDTO = JsonUtils.readJson(req.getReader(), AllSubmissionsRequestDTO.class);
            requestValidator.validateRequest(requestDTO);
            var allSubmissionDTOs = submissionController.getAllSubmissionDTOs(requestDTO);
            var output = JsonUtils.writeJsonAsBytes(allSubmissionDTOs);
            ResponseUtils.callOkResponse(resp, output);
        } catch (UserNotAuthorizedException ex) {
            ResponseUtils.callNotAuthorizedResponse(resp, ex);
        } catch (BaseMonitoringServiceException | JacksonException ex) {
            ResponseUtils.callErrorResponse(resp, ex);
        }
    }

    @Override
    public void init() throws ServletException {
        submissionController = ApplicationComponentsFactory.getSubmissionController();
        requestValidator = ApplicationComponentsFactory.getRequestValidator();
    }
}
