
package com.king.reading.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Debug;

import com.king.reading.SysApplication;

public class DebugUtil {

    private static Boolean sDebuggable;

    private DebugUtil() {
    }

    /**
     * 判断应用是不是DEBUG模式
     * @return true表示为DEBUG模式，false为Release
     */
    public static boolean isDebuggable(Context context) {
        if (sDebuggable == null) {
            ApplicationInfo appInfo = context.getApplicationInfo();
            sDebuggable = (appInfo != null) && ((appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
        }
        return sDebuggable;
    }

    public static boolean isDebuggable() {
        return isDebuggable(SysApplication.getApplication());
    }

    /**
     * 判断是否为调试连接状态
     */
    public static boolean isDebuggerConnected() {
        return Debug.isDebuggerConnected();
    }
}
