package com.ra.hieunt_hn_jv231229_project_module4_api.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends Exception
{
    private HttpStatus httpStatus;

    public CustomException(String message, HttpStatus http)
    {
        super(message);
        this.httpStatus = http;
    }

    public HttpStatus getHttpStatus()
    {
        return this.httpStatus;
    }

    public void setHttpStatus(HttpStatus http)
    {
        this.httpStatus = http;
    }
}
