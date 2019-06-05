package com.project.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionMessage {

    private HttpStatus status;
    private String message;
    private String application;
    private String path;
    private String date;
}
