package ru.ylab.servlet;

import com.fasterxml.jackson.core.JacksonException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import ru.ylab.controller.LoginController;
import ru.ylab.dto.response.MessageDTO;
import ru.ylab.exception.BaseMonitoringServiceException;
import ru.ylab.exception.UserNotAuthorizedException;
import ru.ylab.utils.ApplicationComponentsFactory;
import ru.ylab.utils.JsonUtils;
import ru.ylab.utils.ResponseUtils;

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
