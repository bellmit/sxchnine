package com.project.controller;

import com.project.exception.ExceptionMessage;
import com.project.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ExceptionController {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ExceptionMessage productNotFoundException(HttpServletRequest httpServletRequest, ProductNotFoundException exception){
        return ExceptionMessage.builder().status(HttpStatus.NOT_FOUND)
                .date(LocalDateTime.now().format(FORMATTER))
                .message(exception.getMessage())
                .path(httpServletRequest.getRequestURI()).build();
    }
}
