package com.example.rediscacheable.redis.exception;

/**
 * @author : songtc
 * @since : 2023/12/21 11:10
 */
public class SpELParameterMissException extends RuntimeException{

    public SpELParameterMissException() {
        super();
    }

    public SpELParameterMissException(String message) {
        super(message);
    }

    public SpELParameterMissException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpELParameterMissException(Throwable cause) {
        super(cause);
    }

    protected SpELParameterMissException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
