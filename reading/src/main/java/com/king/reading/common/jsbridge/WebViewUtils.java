package com.king.reading.common.jsbridge;


import com.king.reading.AppInfo;
import com.king.reading.C;
import com.king.reading.common.utils.Check;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebViewUtils {

    public static final String YXYHttpsUrlPrefix = "https://open.yunxiaoyuan.com/connect";
    public static final String YXYDevHttpsUrlPrefix = "https://dev.open.yunxiaoyuan.com/connect";
    public static final String YXYDevHttpUrlPrefix = "http://dev.open.yunxiaoyuan.com/connect";

    public static String getAppendInfoUrl(String url) {
        if (Check.isNotEmpty(url) && (url.startsWith(YXYHttpsUrlPrefix) || url.startsWith(YXYDevHttpsUrlPrefix) || url.startsWith(YXYDevHttpUrlPrefix))) {
            url = addConnectInformation(url);
        }
        return url;
    }

    private static String addConnectInformation(String urlYXY) {
        String urlInforamtion = urlYXY;
        try {
            String string = "appId=" + URLEncoder.encode(C.KINGSUN_APP_ID, "UTF-8")
                    + "&userId=" + URLEncoder.encode("" + "", "UTF-8")
                    + "&versionCode=" + URLEncoder.encode(AppInfo.versionCode + "", "UTF-8")
                    + "&buildNum=" + URLEncoder.encode(AppInfo.buildNum + "", "UTF-8")
                    + "&token=" + URLEncoder.encode("", "UTF-8");
            if (urlYXY.contains("?")) {
                urlInforamtion = urlYXY + "&" + string;
            } else {
                urlInforamtion = urlYXY + "?" + string;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlInforamtion;
    }
}
