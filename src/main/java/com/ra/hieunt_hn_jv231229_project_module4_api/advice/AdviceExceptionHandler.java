package com.ra.hieunt_hn_jv231229_project_module4_api.advice;

import com.ra.hieunt_hn_jv231229_project_module4_api.exception.CustomException;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.CustomResponseEntity;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class AdviceExceptionHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CustomResponseEntity<Map<String, String>> handleNotValidArg(MethodArgumentNotValidException e)
    {
        Map<String, String> errors = new HashMap<>();
        e.getAllErrors().forEach(err -> errors.put(err.getCode() + err.hashCode(), err.getDefaultMessage()));
        HttpStatus http = HttpStatus.BAD_REQUEST;
        return CustomResponseEntity.<Map<String, String>>builder()
                .statusCode(http.value())
                .status(http)
                .data(errors)
                .message("Invalid arguments .Please ensure all sign up fields are correct")
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    public CustomResponseEntity<String> handleBadReq(BadRequestException e)
    {
        HttpStatus http = HttpStatus.BAD_REQUEST;
        return CustomResponseEntity.<String>builder()
                .statusCode(http.value())
                .status(http)
                .data("Bad request")
                .message("Please ensure all sign up fields are correct")
                .build();
    }

    @ExceptionHandler(CustomException.class)
    public CustomResponseEntity<String> handleCustomEx(CustomException e)
    {
        HttpStatus http = HttpStatus.UNAUTHORIZED;
        return CustomResponseEntity.<String>builder()
                .statusCode(http.value())
                .status(http)
                .data(e.getMessage())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public CustomResponseEntity<String> handleMethodNotAllowed(MethodNotAllowedException e)
    {
        HttpStatus http = HttpStatus.METHOD_NOT_ALLOWED;
        return CustomResponseEntity.<String>builder()
                .statusCode(http.value())
                .status(http)
                .data("Method not allowed")
                .message("Method type is not correct")
                .build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public CustomResponseEntity<String> handleNoElement(NoSuchElementException e)
    {
        HttpStatus http = HttpStatus.NO_CONTENT;
        return CustomResponseEntity.<String>builder()
                .statusCode(http.value())
                .status(http)
                .data(e.getMessage())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    public CustomResponseEntity<String> handleRunTime(RuntimeException e)
    {
        HttpStatus http = HttpStatus.INTERNAL_SERVER_ERROR;
        return CustomResponseEntity.<String>builder()
                .statusCode(http.value())
                .status(http)
                .data(e.getMessage())
                .message(e.getMessage())
                .build();
    }
}