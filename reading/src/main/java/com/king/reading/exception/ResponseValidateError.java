package com.king.reading.exception;

/**
 * Created by AllynYonge on 17/07/2017.
 */

public class ResponseValidateError extends RuntimeException {

    public ResponseValidateError() {
    }

    public ResponseValidateError(String message) {
        super(message);
    }

    public ResponseValidateError(String message, Throwable cause) {
        super(message, cause);
    }
}
