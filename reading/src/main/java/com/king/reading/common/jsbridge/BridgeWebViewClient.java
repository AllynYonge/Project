package com.king.reading.common.jsbridge;

import android.graphics.Bitmap;

import com.tencent.smtt.sdk.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by bruce on 10/28/15.
 */
public class BridgeWebViewClient extends WebViewClient {
    private BridgeWebView webView;

    public BridgeWebViewClient(BridgeWebView webView) {
        this.webView = webView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            BridgeWebView.class.cast(webView).handlerReturnData(url);
            return true;
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            BridgeWebView.class.cast(webView).flushMessageQueue();
            return true;
        } else {
            return super.shouldOverrideUrlLoading(webView, url);
        }
    }

    @Override
    public void onPageStarted(com.tencent.smtt.sdk.WebView webView, String s, Bitmap bitmap) {
        super.onPageStarted(webView, s, bitmap);
    }

    @Override
    public void onPageFinished(com.tencent.smtt.sdk.WebView webView, String url) {
        super.onPageFinished(webView, url);

        if (BridgeWebView.toLoadJs != null) {
            BridgeUtil.webViewLoadLocalJs(webView, BridgeWebView.toLoadJs);
        }
        BridgeWebView bridgeWebView = BridgeWebView.class.cast(webView);
        //
        if (bridgeWebView.getStartupMessage() != null) {
            for (Message m : bridgeWebView.getStartupMessage()) {
                bridgeWebView.dispatchMessage(m);
            }
            bridgeWebView.setStartupMessage(null);
        }
    }

    @Override
    public void onReceivedError(com.tencent.smtt.sdk.WebView webView, int i, String description, String failingUrl) {
        super.onReceivedError(webView, i, description, failingUrl);
    }
}