package ru.ylab.servlet;

import com.fasterxml.jackson.core.JacksonException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.ylab.controller.UserController;
import ru.ylab.exception.BaseMonitoringServiceException;
import ru.ylab.exception.UserNotAuthorizedException;
import ru.ylab.utils.ApplicationComponentsFactory;
import ru.ylab.utils.JsonUtils;
import ru.ylab.utils.ResponseUtils;

import java.io.IOException;

@RequiredArgsConstructor
@WebServlet("/api/v1/users/me")
public class CurrentUserServlet extends HttpServlet {
    @Setter
    private UserController userController;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var userDTO = userController.getCurrentUser();
            var output = JsonUtils.writeJsonAsBytes(userDTO);
            ResponseUtils.callOkResponse(resp, output);
        } catch (UserNotAuthorizedException ex) {
            ResponseUtils.callNotAuthorizedResponse(resp, ex);
        } catch (BaseMonitoringServiceException | JacksonException ex) {
            ResponseUtils.callErrorResponse(resp, ex);
        }
    }

    @Override
    public void init() throws ServletException {
        userController = ApplicationComponentsFactory.getUserController();
    }
}
