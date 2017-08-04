package com.king.reading.common.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.king.reading.R;
import com.king.reading.SysApplication;

import java.lang.ref.WeakReference;

/**
 * Created by holenzhou on 16/9/12.
 */

public class SmsCodeHandler extends Handler {

    public final static int CHANGETIME = 1;
    public final static int START_DIFF = 2;
    public final static int STOP = 0;
    public final static int INTERVAL = 60;

    private WeakReference<Activity> mWeakReference;
    private TextView button;
    private int time = INTERVAL;

    public SmsCodeHandler(Activity activity, TextView button) {
        mWeakReference = new WeakReference<Activity>(activity);
        this.button = button;
    }

    @Override
    public void handleMessage(Message msg) {
        Activity activity = mWeakReference.get();
        if (activity != null) {
            switch (msg.what) {
                case CHANGETIME:
                    button.setText(time + "秒后重发");
                    button.setTextColor(SysApplication.getApplication().getResources().getColor(R.color.gray_30));
                    button.setEnabled(false);

                    if (time-- > 1) {
                        sendEmptyMessageDelayed(CHANGETIME, 1000);
                    } else {
                        sendEmptyMessageDelayed(STOP, 1000);
                    }
                    break;

                case START_DIFF:
                    time = msg.arg1;
                    if (time >= 1) {
                        sendEmptyMessage(CHANGETIME);
                    }
                    break;

                case STOP:
                    button.setEnabled(true);
                    button.setTextColor(SysApplication.getApplication().getResources().getColor(R.color.cyan));
                    button.setText("获取验证码");
                    time = INTERVAL;
                    break;
            }
        }
    }
}
