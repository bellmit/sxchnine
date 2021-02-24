package com.project.controller;

import com.project.exception.ConfirmPasswordException;
import com.project.exception.ExceptionMessage;
import com.project.exception.IncorrectPasswordException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.view.RequestContext;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestControllerAdvice
public class UserAdviceController {


    @ExceptionHandler(value = ConfirmPasswordException.class)
    public Mono<ExceptionMessage> confirmPasswordException(ConfirmPasswordException confirmPasswordException){
        return Mono.just(new ExceptionMessage(confirmPasswordException.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(value = IncorrectPasswordException.class)
    public Mono<ExceptionMessage> incorrectPasswordException(IncorrectPasswordException incorrectPasswordException){
        return  Mono.just(new ExceptionMessage(incorrectPasswordException.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus
    public Mono<ExceptionMessage> exception(Exception exception){
        return  Mono.fromCallable(() -> new ExceptionMessage(exception.getMessage(), LocalDateTime.now()));
    }
}
