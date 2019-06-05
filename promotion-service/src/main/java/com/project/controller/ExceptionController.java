package com.project.controller;

import com.project.exception.ExceptionMessage;
import com.project.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ExceptionController {

    @Value("${application.name}")
    private String applicationName;

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage productNotFoundExceptionHandler(HttpServletRequest request, ProductNotFoundException ex){
        return ExceptionMessage.builder()
                .date(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .application(applicationName)
                .path(request.getRequestURI())
                .build();
    }
}
