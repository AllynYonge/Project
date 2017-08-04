package com.king.reading.exception;

/**
 * Created by hu.yang on 2017/5/19.
 */

public class ParseResponseException extends RuntimeException{

    public ParseResponseException() {
    }

    public ParseResponseException(String message) {
        super(message);

    }

    public ParseResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseResponseException(Throwable cause) {
        super(cause);
    }
}
