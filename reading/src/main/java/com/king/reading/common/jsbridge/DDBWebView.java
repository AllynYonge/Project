package com.king.reading.common.jsbridge;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.king.reading.AppInfo;
import com.king.reading.common.utils.Check;
import com.king.reading.data.entities.UserEntity;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;

import org.json.JSONException;
import org.json.JSONObject;

public class DDBWebView extends ProgressWebView {


    private OnTitleChangeListener onTitleChangeListener;
    private onProgressListener mOnProgressListener;

    public static final String JS_VERSION = "5.1";
    private ReloadNativeListener reloadNativeListener;
    private PageLoadCallBack mPageLoadCallBack;

    public DDBWebView(Context context) {
        super(context);
        initWebView();
    }

    public DDBWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    private void initWebView() {
        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            fixWebView();
        }
        setWebChromeClient(new CustomChromeClient());

        registerJSBridgeHandler();

        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // zoom支持
        getSettings().setBuiltInZoomControls(true);
        getSettings().setDisplayZoomControls(false);

        getSettings().setDomStorageEnabled(true);
        // 设置缓存总容量为 8mb
        getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        // 缓存路径
        String appCachePath = getContext().getCacheDir().getAbsolutePath();
        getSettings().setAppCachePath(appCachePath);
        getSettings().setAllowFileAccess(true);
        getSettings().setAppCacheEnabled(true);
        // 设置缓存策略
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        setDefaultHandler(new DDBHandler());
        // 在UserAgent中添加云校园标识
        getSettings().setUserAgentString(getSettings().getUserAgentString() + "DDB");
    }

    @TargetApi(11)
    private void fixWebView() {
        // We hadn't use addJavascriptInterface, but WebView add "searchBoxJavaBridge_" to mJavaScriptObjects
        // below API 17 by default:
        // mJavaScriptObjects.put(SearchBoxImpl.JS_INTERFACE_NAME, mSearchBox);
        removeJavascriptInterface("searchBoxJavaBridge_");
    }

    protected void registerJSBridgeHandler() {
        registerHandler("getUserInfo", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Logger.i("handler = getUserInfo, data from web = " + data);
                JSONObject json = new JSONObject();
                try {
                    UserEntity userInfo = new UserEntity();
                    if (userInfo != null) {
                        json.put("ret", true);
                        JSONObject dataJson = new JSONObject();
                        json.put("data", dataJson);
                    } else {
                        json.put("ret", false);
                        json.put("data", "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                function.onCallBack(json.toString());
            }
        });

        registerHandler("getUserToken", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Logger.i("handler = getUserToken, data from web = " + data);
                JSONObject json = new JSONObject();
                try {
                    json.put("ret", true);

                    JSONObject dataJson = new JSONObject();
                    UserEntity user = new UserEntity();
                    json.put("data", dataJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                function.onCallBack(json.toString());
            }
        });

        registerHandler("getQUA", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Logger.i("handler = getQUA, data from web = " + data);
                JSONObject json = new JSONObject();
                try {
                    json.put("ret", true);

                    JSONObject dataJson = new JSONObject();
                    dataJson.put("QUA", AppInfo.getInstance().getQUA());
                    json.put("data", dataJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                function.onCallBack(json.toString());
            }
        });

        registerHandler("setNavigationButtonsHidden", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Logger.i("handler = setNavigationButtonsHidden, data from web = " + data);
                JSONObject json = new JSONObject();
                try {
                    json.put("ret", true);
                    json.put("data", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                function.onCallBack(json.toString());
            }
        });

        /**
         * 注册JSBridge的版本，用于兼容升级后的js接口
         * Js bridge version >= 1.1 有照相功能
         * Js bridge version >= 1.2 有支付功能
         * Js bridge version >= 1.3 资讯详情页面相关接口
         * Js bridge version >= 1.4 资讯详情页面点赞
         * Js bridge version >= 2.0 资讯详情页面点赞
         */
        registerHandler("getJSBridgeVersion", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Logger.i("handler = getJSBridgeVersion, data from web = " + data);
                JSONObject json = new JSONObject();
                try {
                    json.put("ret", true);
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("version", JS_VERSION);
                    json.put("data", dataJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                function.onCallBack(json.toString());
            }
        });


        registerHandler("reloadNative", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (Check.isNotEmpty(data)) {
                    Logger.d("reloadNative, data: " + data);
                    if (reloadNativeListener != null) {
                        reloadNativeListener.onReload();
                    }
                }
            }
        });
        
        send("hi");
    }

    public void setOnReloadNativeListener(ReloadNativeListener listener) {
        this.reloadNativeListener = listener;
    }

    public class CustomChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(com.tencent.smtt.sdk.WebView webView, String s, String s1, com.tencent.smtt.export.external.interfaces.JsResult jsResult) {
            return super.onJsAlert(webView, s, s1, jsResult);
        }

        @Override
        public void onProgressChanged(com.tencent.smtt.sdk.WebView webView, int newProgress) {
            super.onProgressChanged(webView, newProgress);
            Logger.d("progress:" + newProgress);
            showProgress(newProgress);
        }

        @Override
        public boolean onJsPrompt(com.tencent.smtt.sdk.WebView webView, String s, String s1, String s2, com.tencent.smtt.export.external.interfaces.JsPromptResult jsPromptResult) {
            return super.onJsPrompt(webView, s, s1, s2, jsPromptResult);
        }

        @Override
        public void onReceivedTitle(com.tencent.smtt.sdk.WebView webView, String title) {
            super.onReceivedTitle(webView, title);
            Logger.d("setTitle:" + title);
            if (onTitleChangeListener != null) {
                onTitleChangeListener.onSetTitle(title);
            }
        }

    }

    private void showProgress(int progress) {
        if (progress >= 100) {
            getProgressBar().setVisibility(View.GONE);
            if(mOnProgressListener != null) mOnProgressListener.loadComplete();
        } else {
            if (getProgressBar().getVisibility() == View.GONE) {
                getProgressBar().setVisibility(View.VISIBLE);
            }
            getProgressBar().setProgress(progress);
        }
    }

    /**
     * 检查SD卡是否存在
     *
     * @return
     */
    public final boolean checkSDcard() {
        boolean flag = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (!flag) {
            Toast.makeText(getContext(), "请插入手机存储卡再使用本功能", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    public interface OnTitleChangeListener {

        void onSetTitle(String title);
    }

    public interface ReloadNativeListener {
        void onReload();
    }
    
    public interface onProgressListener {
        void loadComplete();
    }

    public void setOnTitleChangeListener(OnTitleChangeListener l) {
        this.onTitleChangeListener = l;
    }
    
    public void setOnprogressListener(onProgressListener listener) {
        mOnProgressListener = listener;
    }

    public void setPageLoadCallback(PageLoadCallBack callback){
        mPageLoadCallBack = callback;
    }

    @Override
    protected BridgeWebViewClient generateBridgeWebViewClient() {
        return new DDBWebViewClient(this);
    }

    private class DDBWebViewClient extends BridgeWebViewClient{

        public DDBWebViewClient(BridgeWebView webView) {
            super(webView);
        }

        @Override
        public void onPageFinished(com.tencent.smtt.sdk.WebView webView, String url) {
            super.onPageFinished(webView, url);
            if (mPageLoadCallBack != null){
                mPageLoadCallBack.loadFinish(url);
            }
        }

        @Override
        public void onReceivedError(com.tencent.smtt.sdk.WebView webView, int i, String description, String failingUrl) {
            super.onReceivedError(webView, i, description, failingUrl);
            Logger.d("onReceivedError[errorCode: " + i + ", description: " + description + ", failingUrl: " + failingUrl + "]");
            try {
                webView.stopLoading();
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
            try {
                clearView();
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
            if (mPageLoadCallBack != null){
                mPageLoadCallBack.loadError(failingUrl);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String url) {
            if (url.startsWith("ddb://")){
                ARouter.getInstance().build(Uri.parse(url)).navigation();
                return true;
            }
            return super.shouldOverrideUrlLoading(webView, url);
        }

    }

}
