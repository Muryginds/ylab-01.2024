package io.ylab.servlet;

import com.fasterxml.jackson.core.JacksonException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import io.ylab.controller.LoginController;
import io.ylab.dto.request.UserAuthorizationRequestDto;
import io.ylab.exception.BaseMonitoringServiceException;
import io.ylab.utils.ApplicationComponentsFactory;
import io.ylab.utils.ResponseUtils;
import io.ylab.utils.JsonUtils;
import io.ylab.utils.RequestValidator;

import java.io.IOException;

@WebServlet("/api/v1/auth/login")
public class LoginServlet extends HttpServlet {
    @Setter
    private LoginController loginController;
    @Setter
    private RequestValidator requestValidator;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var requestDTO = JsonUtils.readJson(req.getReader(), UserAuthorizationRequestDto.class);
            requestValidator.validateRequest(requestDTO);
            var userDTO = loginController.authorize(requestDTO);
            var output = JsonUtils.writeJsonAsBytes(userDTO);
            ResponseUtils.callOkResponse(resp, output);
        } catch (BaseMonitoringServiceException | JacksonException ex) {
            ResponseUtils.callErrorResponse(resp, ex);
        }
    }

    @Override
    public void init() throws ServletException {
        loginController = ApplicationComponentsFactory.getLoginController();
        requestValidator = ApplicationComponentsFactory.getRequestValidator();
    }
}
