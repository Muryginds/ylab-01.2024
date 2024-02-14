package io.ylab.servlet;

import com.fasterxml.jackson.core.JacksonException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import io.ylab.controller.LoginController;
import io.ylab.dto.response.MessageDTO;
import io.ylab.exception.BaseMonitoringServiceException;
import io.ylab.exception.UserNotAuthorizedException;
import io.ylab.utils.ApplicationComponentsFactory;
import io.ylab.utils.JsonUtils;
import io.ylab.utils.ResponseUtils;

import java.io.IOException;

@WebServlet("/api/v1/auth/logout")
public class LogoutServlet extends HttpServlet {
    @Setter
    private LoginController loginController;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            loginController.logout();
            var responseDTO = MessageDTO.builder().message("user logged out").build();
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
        loginController = ApplicationComponentsFactory.getLoginController();
    }
}
