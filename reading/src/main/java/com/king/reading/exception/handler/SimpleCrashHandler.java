package com.king.reading.exception.handler;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.king.reading.AppInfo;
import com.king.reading.common.utils.FileUtils;
import com.king.reading.common.utils.NetworkUtils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hu.yang on 2017/5/18.
 */

public class SimpleCrashHandler extends BaseCrashHandler{
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");
    private StringBuilder stringBuilder = new StringBuilder();

    public SimpleCrashHandler(BaseCrashHandlerBuilder builder) {
        super(builder);
    }

    @Override
    public boolean handleException(Throwable paramThrowable) {
        if (paramThrowable == null)
            return false;
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, mContext.getText(crashPromptStringId), Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();

        if (isWrite){
            saveDeviceInfo();
            saveCrashLogToFile(paramThrowable);
        }
        return true;
    }

    private String saveCrashLogToFile(Throwable paramThrowable) {
        Writer mWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mWriter);
        paramThrowable.printStackTrace(mPrintWriter);
        paramThrowable.printStackTrace();
        Throwable mThrowable = paramThrowable.getCause();
        while (mThrowable != null) {
            mThrowable.printStackTrace(mPrintWriter);
            mPrintWriter.append("\r\n");
            mThrowable = mThrowable.getCause();
        }
        mPrintWriter.close();
        String mResult = mWriter.toString();

        stringBuilder.append(mResult);

        String mTime = mSimpleDateFormat.format(new Date());
        String mFileName = mTime + ".txt";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File logFolder = FileUtils.getLogFolder(mContext);
            File logFile = new File(logFolder,mFileName);
            FileUtils.writeStringToFile(logFile, stringBuilder.toString(), "UTF-8");
        }
        return null;
    }

    public void saveDeviceInfo() {
        append("versionName", AppInfo.versionName);
        append("versionCode",String.valueOf(AppInfo.versionCode));
        append("imei", AppInfo.imei);
        append("mac", AppInfo.mac);
        append("sdkName", AppInfo.sdkName);
        append("sdkVersion",String.valueOf(AppInfo.sdkVersion));
        append("brand", AppInfo.brand);
        append("model", AppInfo.model);
        append("buildNum",String.valueOf(AppInfo.buildNum));
        append("channel", AppInfo.channel);
        append("cpu_ABI",Build.CPU_ABI);
        append("cpu_ABI2",Build.CPU_ABI2);
        append("netType",getNetState());
        stringBuilder.append("\r\n--------------------------\n");
    }

    private void append(String key, String value){
        stringBuilder.append(key + "=" + value + "\r\n");
    }

    private String getNetState(){
        switch (NetworkUtils.getConnectedType(mContext)){
            case NETWORKTYPE_WAP:
                return "WAP";
            case NETWORKTYPE_2G:
                return "2G";
            case NETWORKTYPE_3G:
                return "3G";
            case NETWORKTYPE_4G:
                return "4G";
            case NETWORKTYPE_WIFI:
                return "WIFI";
            default:
                return "unknown";

        }
    }

    public static SimpleCrashHandlerBuilder createBuilder(Context context) {
        return new SimpleCrashHandlerBuilder(context);
    }

    public static class SimpleCrashHandlerBuilder extends BaseCrashHandlerBuilder<SimpleCrashHandlerBuilder>{

        public SimpleCrashHandlerBuilder(Context context) {
            super(context);
        }

        @Override
        public BaseCrashHandler create() {
            return new SimpleCrashHandler(this);
        }
    }

}
