package com.king.reading.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 创建者     王开冰
 * 创建时间   2017/7/24 13:55
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class SpUtils {
    /**
     * 利用ShareedPreference保存必要信息
     *
     * @param ctx   上下文Context
     * @param name  要保存信息的名字
     * @param value 要保存的值
     */
    public static void sharePreSave(Context ctx, String name, String value) {
        SharedPreferences pref = ctx.getSharedPreferences("prefKingReading", Context.MODE_PRIVATE);
        // SharedPreferences pref = LoginActivity.mPref;
        pref.edit().putString(name, value).commit();

    }

    public static void sharePreSaveInt(Context ctx, String name, int value) {
        SharedPreferences pref = ctx.getSharedPreferences("prefKingReading", Context.MODE_PRIVATE);
        pref.edit().putInt(name, value).commit();
    }

    public static void sharePreSaveFloat(Context ctx, String name, float value) {
        SharedPreferences pref = ctx.getSharedPreferences("prefKingReading", Context.MODE_PRIVATE);
        pref.edit().putFloat(name, value).commit();
    }

    /**
     * 利用ShareedPreference保存必要信息
     *
     * @param ctx   上下文Context
     * @param name  要保存信息的名字
     * @param value 要保存的值
     */
    public static void sharePreSaveBoolean(Context ctx, String name, Boolean value) {
        SharedPreferences pref = ctx.getSharedPreferences("prefKingReading", Context.MODE_PRIVATE);
        // SharedPreferences pref = LoginActivity.mPref;
        pref.edit().putBoolean(name, value).commit();
    }

    /**
     * 获取ShareedPreference保存的信息
     *
     * @param ctx  上下文Context
     * @param name 所保存信息的名字
     */
    public static String sharePreGet(Context ctx, String name) {
        SharedPreferences pref = ctx.getSharedPreferences("prefKingReading", Context.MODE_PRIVATE);
        // SharedPreferences pref = LoginActivity.mPref;

        return pref.getString(name, null);// 不能改成""，不然进入到点读 跟读页面，一片空白

    }

    /**
     * 获取ShareedPreference保存的信息
     *
     * @param ctx  上下文Context
     * @param name 所保存信息的名字
     */
    public static boolean sharePreGetBoolean(Context ctx, String name) {
        SharedPreferences pref = ctx.getSharedPreferences("prefKingReading", Context.MODE_PRIVATE);
        // SharedPreferences pref = LoginActivity.mPref;
        return pref.getBoolean(name, false);
    }

    public static int sharePreGetInt(Context ctx, String name) {
        SharedPreferences pref = ctx.getSharedPreferences("prefKingReading", Context.MODE_PRIVATE);
        // SharedPreferences pref = LoginActivity.mPref;
        return pref.getInt(name, 0);
    }

    public static float sharePreGetFloat(Context ctx, String name) {
        SharedPreferences pref = ctx.getSharedPreferences("prefKingReading", Context.MODE_PRIVATE);
        // SharedPreferences pref = LoginActivity.mPref;
        return pref.getFloat(name, 1.0f);
    }

    /**
     * 删除ShareedPreference保存的信息
     *
     * @param ctx  上下文Context
     * @param name 所要删除信息的名字
     */
    public static void sharePreRemo(Context ctx, String name) {
        SharedPreferences pref = ctx.getSharedPreferences("prefKingReading", Context.MODE_PRIVATE);
        // SharedPreferences pref = LoginActivity.mPref;
        pref.edit().remove(name).commit();
    }
}
