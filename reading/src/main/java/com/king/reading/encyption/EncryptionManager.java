package com.king.reading.encyption;

/**
 * Created by tom on 2014/12/19.
 */
public class EncryptionManager {

    private static IEncryption mInstance = new DES3();

    public static final String ENCYPTION_TYPE_DES3 = "DES3";
    public static final String ENCYPTION_TYPE_TEA = "TEA";

    public static IEncryption getEncyption() {
        return mInstance;
    }

    public static IEncryption getEncyption(String type) {
        if (ENCYPTION_TYPE_DES3.equals(type)) {
            return new DES3();
        } else if (ENCYPTION_TYPE_TEA.equals(type)) {
            return new Tea();
        }
        return mInstance;
    }
}
