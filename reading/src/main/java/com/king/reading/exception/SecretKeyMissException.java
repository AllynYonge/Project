package com.king.reading.exception;

/**
 * Created by AllynYonge on 27/07/2017.
 */

public class SecretKeyMissException extends RuntimeException{

    public SecretKeyMissException() {
        super("解密资源时为设置密钥");
    }
}
