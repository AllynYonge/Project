package com.king.reading.common.jsbridge;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.king.reading.R;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.common.utils.Check;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

import static com.king.reading.C.ROUTER_WEB;

@Route(path = ROUTER_WEB)
public class WebActivity extends BaseActivity implements PageLoadCallBack{

    public static final String JS_VERSION = "2.0";
    @Autowired
    String url;
    @Autowired
    String title;

    @BindView(R.id.webView)
    protected DDBWebView mWebView;

    @Override
    public void onInitView() {
        //视频为了避免闪屏和透明问题
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        ARouter.getInstance().inject(this);
        setCenterTitle(Check.isEmpty(title) ? "载入中…" : title);
        setLeftImageIcon(R.mipmap.ic_back);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mWebView.setPageLoadCallback(this);
        mWebView.setOnTitleChangeListener(new DDBWebView.OnTitleChangeListener() {

            @Override
            public void onSetTitle(String title) {
                setCenterTitle(title);
            }
        });
        loadUrl();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        ARouter.getInstance().inject(this);
        loadUrl();
    }

    private void loadUrl() {
        if (mWebView != null && Check.isNotEmpty(url)) {
            mWebView.loadUrl(url);
            Logger.d("load url: " + url);
        }
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_web;
    }

    private void updateTitleLeftCloseBtn() {
        if (mWebView != null && mWebView.canGoBack()) {
            setLeftImageIcon(R.mipmap.ic_back);
        }
    }

    public void setWebPageTitle(String title) {
        setCenterTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    public void onLeftClick(View view) {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            showContent();
        } else {
            super.onLeftClick(view);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            showContent();
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void loadError(final String failUrl) {
        setCenterTitle("页面加载失败");
        showError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("reload: " + mWebView.getUrl());
                if (mWebView.getUrl() == null) {
                    mWebView.loadUrl(failUrl);
                } else {
                    mWebView.reload();
                }
            }
        });
    }

    @Override
    public void loadFinish(String url) {
        updateTitleLeftCloseBtn();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String title = Check.isEmpty(getTitle()) ? url : mWebView.getTitle();
            setCenterTitle(title);
        }
    }
}
