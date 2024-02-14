package ru.ylab.servlet;

import com.fasterxml.jackson.core.JacksonException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import ru.ylab.controller.LoginController;
import ru.ylab.dto.request.UserAuthorizationRequestDTO;
import ru.ylab.exception.BaseMonitoringServiceException;
import ru.ylab.utils.ApplicationComponentsFactory;
import ru.ylab.utils.ResponseUtils;
import ru.ylab.utils.JsonUtils;
import ru.ylab.utils.RequestValidator;

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
            var requestDTO = JsonUtils.readJson(req.getReader(), UserAuthorizationRequestDTO.class);
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