package com.igdcbot.AuthCode.controller.error;

import com.igdcbot.AuthCode.entity.response.Response;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public Response<String> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                Response<String> notFoundResponse = new Response<String>();
                notFoundResponse.setStatus(String.valueOf(HttpStatus.NOT_FOUND.value()));
                notFoundResponse.setMessage("Resources not found.");
                notFoundResponse.setData(null);
                return notFoundResponse;
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                Response<String> serverErrorResponse = new Response<String>();
                serverErrorResponse.setStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
                serverErrorResponse.setMessage("Internal server error. Reason: " +
                        ((Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION)).getMessage());
                serverErrorResponse.setData(null);
                return serverErrorResponse;
            }
        }

        Response<String> unknownErrorResponse = new Response<String>();
        unknownErrorResponse.setStatus(null);
        unknownErrorResponse.setMessage("Unknown server error.");
        unknownErrorResponse.setData(null);
        return unknownErrorResponse;
    }

}