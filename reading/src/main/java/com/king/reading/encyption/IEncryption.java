package com.king.reading.encyption;

/**
 * Created by tom on 2014/12/19.
 */
public interface IEncryption {

    byte[] encryption(byte[] bytes);
    byte[] decryption(byte[] bytes);
}
