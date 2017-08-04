package com.king.reading.common.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by AllynYonge on 07/07/2017.
 */

public class EncryptUtil {
    private static Cipher cipher;

    /**
     * DES加密算法
     * @param secretKey    密钥
     * @param mode   模式： 加密，解密
     * @param data   需要加密的内容
     * @return     将内容加密后的结果也是byte[]格式的
     */
    public static byte[] des(String secretKey, int mode,byte[] data)
    {
        byte[] keyByte = secretKey.getBytes();
        byte[] ret = null;
        //加密的内容存在并且密钥存在且长度为8个字节
        if (data != null
                && data.length>0
                && keyByte !=null
                && keyByte.length==8) {
            try {
                if (cipher == null){
                    cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                }
                DESKeySpec keySpec = new DESKeySpec(keyByte);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey key = keyFactory.generateSecret(keySpec);
                cipher.init(mode, key);
                ret = cipher.doFinal(data);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    //DES 加密
    public static byte[] desEncrypt(String key, byte[] data){
        return des(key, Cipher.ENCRYPT_MODE, data);
    }
    //DES 解密
    public static byte[] desDecrypt(String key, byte[] data){
        return des(key, Cipher.DECRYPT_MODE,data);
    }

}
