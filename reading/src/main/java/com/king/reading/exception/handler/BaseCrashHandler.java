package com.king.reading.exception.handler;

import android.content.Context;

import com.king.reading.common.utils.AppUtils;

import java.lang.Thread.UncaughtExceptionHandler;

public abstract class BaseCrashHandler implements UncaughtExceptionHandler {

    private UncaughtExceptionHandler mDefaultHandler;
    protected Context mContext;
    protected int crashPromptStringId;
    protected boolean isWrite;

    public BaseCrashHandler(BaseCrashHandlerBuilder builder) {
        this.mContext = builder.mContext;
        this.isWrite = builder.isWrite;
        this.crashPromptStringId = builder.crashPromptStringId;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
        if (!handleException(paramThrowable) && mDefaultHandler != null) {
            //系统处理
            mDefaultHandler.uncaughtException(paramThread, paramThrowable);
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AppUtils.exitApp();
        }
    }

    public abstract boolean handleException(Throwable paramThrowable);
}
