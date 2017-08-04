package com.king.reading.exception.handler;

import android.content.Context;

/**
 * Created by hu.yang on 2017/5/18.
 */

public abstract class BaseCrashHandlerBuilder<T> {
    public Context mContext;
    public boolean isWrite;
    public int crashPromptStringId;

    public BaseCrashHandlerBuilder(Context context) {
        this.mContext = context;
    }

    public BaseCrashHandlerBuilder setIsWriteLog(boolean isWrite) {
        this.isWrite = isWrite;
        return this;
    }

    public BaseCrashHandlerBuilder setAppCrashPromptString(int promptString){
        this.crashPromptStringId = promptString;
        return this;
    }

    public abstract BaseCrashHandler create();

}
