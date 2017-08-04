package com.king.reading.common.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.king.reading.C;
import com.king.reading.SysApplication;
import com.king.reading.ddb.PageContext;
import com.king.reading.model.Follow;
import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;

import java.util.List;

/**
 * Created by AllynYonge on 04/07/2017.
 */

public class ACacheMgr {

    private static void savePageContext(String Key, PageContext context) {
        TarsOutputStream stream = new TarsOutputStream();
        context.writeTo(stream);
        getACache().put(Key, stream.toByteArray());
    }

    private static PageContext getPageContext(String Key) {
        TarsInputStream stream = new TarsInputStream(getACache().getAsBinary(Key));
        PageContext context = new PageContext();
        context.readFrom(stream);
        return context;
    }

    public static PageContext getNoticesPageContext() {
        return getPageContext(C.Cache_NOTICE_KEY);
    }

    public static PageContext getActivitiesPageContext() {
        return getPageContext(C.Cache_ACTIVITY_KEY);
    }

    public static void saveNoticesPageContext(PageContext context) {
        savePageContext(C.Cache_NOTICE_KEY, context);
    }

    public static void saveActivitiesPageContext(PageContext context) {
        savePageContext(C.Cache_ACTIVITY_KEY, context);
    }

    public static void saveBreakThroughtFollow(String key, List<Follow> follows) {

        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        for (Follow follow  : follows) {
            jsonArray.add(follow);
        }
        Log.d("saveBreakThroughtFollow","jsonArray="+jsonArray.toString());
        getACache().put(key, jsonArray);
    }

    public static List<Follow> getBreakThroughtFollow(String key) {

        com.alibaba.fastjson.JSONArray jsonArray = (JSONArray) getACache().getAsObject(key);
        List<Follow> follows = jsonArray.toJavaList(Follow.class);
        return follows;
    }

    public static ACache getACache() {
        return ACache.get(SysApplication.getApplication());
    }
}
