package com.project.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ExceptionMessage {

    private HttpStatus status;
    private String message;
    private String path;
    private String date;
}
