package com.king.reading.exception;

/**
 * Created by AllynYonge on 12/07/2017.
 */

public class BusinessException extends RuntimeException{
    public BusinessException(String message, int errCode) {
        super(message);
        this.errCode = errCode;
    }

    private int errCode = -1;

    public int getErrCode(){
        return errCode;
    }

}
