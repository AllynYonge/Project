package com.king.reading.common.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.king.reading.SysApplication;

public class ToastUtils {

    public static void show(int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    public static void show(CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void show(final int resId, final int duration) {
        String text = SysApplication.getApplication().getString(resId);
        show(text, duration);
    }

    public static void show(final CharSequence text, final int duration) {
        SysApplication.getMainThreadHandler().post(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SysApplication.getApplication(), text, duration).show();
                    }
                });
    }

    public static void show(int resId, Object... args) {
        show(String.format(SysApplication.getApplication().getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void show(String format, Object... args) {
        show(String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(int resId, int duration, Object... args) {
        show(String.format(SysApplication.getApplication().getResources().getString(resId), args), duration);
    }

    public static void show(String format, int duration, Object... args) {
        show(String.format(format, args), duration);
    }

    public static void showInCenter(int resId) {
        showInCenter(SysApplication.getApplication().getString(resId));
    }

    public static void showInCenter(final String string) {
        SysApplication.getMainThreadHandler().post(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(SysApplication.getApplication(), string, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
    }

    public static void DebugToast(int resId) {
        if (DebugUtil.isDebuggable()) {
            DebugToast(SysApplication.getApplication().getString(resId));
        }
    }

    public static void DebugToast(String text) {
        if (DebugUtil.isDebuggable()) {
            show("debug模式: \r\n " + text);
        }
    }

    /**
     * 自定义Toast
     */
    public static void showCustomToast(final Context context, final int layoutId, final int textViewId, final String content) {
        SysApplication.getMainThreadHandler().post(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = createCustomToast(context, layoutId, textViewId, content, 0, null);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }


    public static void showCenterCustomToast(final Context context, final int layoutId, final int textViewId,
                                             final String content) {
        SysApplication.getMainThreadHandler().post(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = createCustomToast(context, layoutId, textViewId, content, 0, null);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
    }

    public static void showCenterCustomToastWithTilte(final Context context, final int layoutId, final int textViewId,
                                                      final String content, final int TitleTvId, final String title) {
        SysApplication.getMainThreadHandler().post(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = createCustomToast(context, layoutId, textViewId, content, TitleTvId, title);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
    }

    private static Toast createCustomToast(Context context, int layoutId, int contentTvId, String content, int titleTvId, String title) {
        Toast toast = new Toast(context);
        View layout = View.inflate(context, layoutId, null);
        if (title != null) {
            TextView titleTV = (TextView) layout.findViewById(titleTvId);
            titleTV.setText(title);
        }
        TextView contentTv = (TextView) layout.findViewById(contentTvId);
        contentTv.setText(content);
        toast.setView(layout);
        return toast;
    }

    /**
     * 样式不变,toast的位置展示在正中心
     */
    public static void showCenterToast(String content) {
        Toast toast = Toast.makeText(SysApplication.getApplication(), content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
