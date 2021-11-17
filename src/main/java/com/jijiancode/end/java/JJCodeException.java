package com.jijiancode.end.java;

public class JJCodeException extends RuntimeException{

    public JJCodeException(String message) {
        super(message);
    }

    public JJCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JJCodeException(Throwable cause) {
        super(cause);
    }

    public JJCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
