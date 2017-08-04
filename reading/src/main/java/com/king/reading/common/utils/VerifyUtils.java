package com.king.reading.common.utils;

/**
 * Created by AllynYonge on 12/07/2017.
 */

public class VerifyUtils {
    public static boolean verifyPwd(String password){
        if (password.matches("^[0-9A-Za-z]{6,20}$"))
            return true;

        ToastUtils.show("密码格式不正确");
        return false;
    }

    public static boolean verifyPhoneNumber(String phone){
        if (phone.matches("^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$")){
            return true;
        }
        ToastUtils.show("手机号错误");
        return false;
    }
}
