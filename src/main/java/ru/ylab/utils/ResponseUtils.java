package ru.ylab.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import ru.ylab.dto.response.ErrorDTO;

import java.io.IOException;

@UtilityClass
public class ResponseUtils {
    public void callOkResponse(
            HttpServletResponse response,
            byte[] output
    ) throws IOException {
        prepareResponse(response, HttpServletResponse.SC_OK, output);
    }

    public void callErrorResponse(
            HttpServletResponse response,
            Exception ex
    ) throws IOException {
        var errorDTO = ErrorDTO.builder().error(ex.getMessage()).build();
        var output = JsonUtils.writeJsonAsBytes(errorDTO);
        prepareResponse(response, HttpServletResponse.SC_BAD_REQUEST, output);
    }

    public void callNotAuthorizedResponse(
            HttpServletResponse response,
            Exception ex
    ) throws IOException {
        var errorDTO = ErrorDTO.builder().error(ex.getMessage()).build();
        var output = JsonUtils.writeJsonAsBytes(errorDTO);
        prepareResponse(response, HttpServletResponse.SC_UNAUTHORIZED, output);
    }

    private void prepareResponse(
            HttpServletResponse response,
            Integer statusCode,
            byte[] output
    ) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getOutputStream().write(output);
    }
}
