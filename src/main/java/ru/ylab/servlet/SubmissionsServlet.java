package ru.ylab.servlet;

import com.fasterxml.jackson.core.JacksonException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import ru.ylab.controller.ReadingsRecordingController;
import ru.ylab.controller.SubmissionController;
import ru.ylab.dto.request.NewReadingsSubmissionRequestDTO;
import ru.ylab.dto.request.SubmissionRequestDTO;
import ru.ylab.dto.response.MessageDTO;
import ru.ylab.exception.BaseMonitoringServiceException;
import ru.ylab.exception.UserNotAuthorizedException;
import ru.ylab.utils.ApplicationComponentsFactory;
import ru.ylab.utils.JsonUtils;
import ru.ylab.utils.ResponseUtils;
import ru.ylab.utils.RequestValidator;

import java.io.IOException;

@WebServlet("/api/v1/submissions")
public class SubmissionsServlet extends HttpServlet {
    @Setter
    private SubmissionController submissionController;
    @Setter
    private ReadingsRecordingController readingsRecordingController;
    @Setter
    private RequestValidator requestValidator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var requestDTO = JsonUtils.readJson(req.getReader(), SubmissionRequestDTO.class);
            requestValidator.validateRequest(requestDTO);
            var submissionDTO = submissionController.getSubmissionDTO(requestDTO);
            var output = JsonUtils.writeJsonAsBytes(submissionDTO);
            ResponseUtils.callOkResponse(resp, output);
        } catch (UserNotAuthorizedException ex) {
            ResponseUtils.callNotAuthorizedResponse(resp, ex);
        } catch (BaseMonitoringServiceException | JacksonException ex) {
            ResponseUtils.callErrorResponse(resp, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var requestDTO = JsonUtils.readJson(req.getReader(), NewReadingsSubmissionRequestDTO.class);
            requestValidator.validateRequest(requestDTO);
            readingsRecordingController.saveNewSubmission(requestDTO);
            var responseDTO = MessageDTO.builder().message("new readings saved").build();
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
        submissionController = ApplicationComponentsFactory.getSubmissionController();
        readingsRecordingController = ApplicationComponentsFactory.getReadingsRecordingController();
        requestValidator = ApplicationComponentsFactory.getRequestValidator();
    }
}
