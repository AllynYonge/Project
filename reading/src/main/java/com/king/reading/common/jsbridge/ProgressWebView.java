package com.king.reading.common.jsbridge;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.king.reading.R;
import com.king.reading.widget.drawer.util.DensityUtils;

public class ProgressWebView extends BridgeWebView {

    private ProgressBar mProgressBar;
    private OnScrollChangeListener changeListener;
    private boolean showProgress;

    public ProgressWebView(Context context) {
        super(context);
        init(context);
    }

    @SuppressWarnings("deprecation")
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(getContext(),3)));
        Drawable drawable = context.getResources().getDrawable(R.drawable.webview_progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);

        addView(mProgressBar);
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

}