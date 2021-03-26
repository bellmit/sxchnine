package com.project.exception;

public class CatchupOrdersException extends RuntimeException {

    public CatchupOrdersException(String message) {
        super(message);
    }

    public CatchupOrdersException(String message, Throwable cause) {
        super(message, cause);
    }
}
