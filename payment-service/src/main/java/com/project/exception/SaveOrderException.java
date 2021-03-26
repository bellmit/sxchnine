package com.project.exception;

public class SaveOrderException extends RuntimeException{

    public SaveOrderException(String message) {
        super(message);
    }

    public SaveOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
