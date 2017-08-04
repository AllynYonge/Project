package com.king.reading.module.user;

import android.os.Bundle;

import com.king.reading.R;
import com.king.reading.base.activity.BaseActivity;

/**
 * Created by AllynYonge on 22/06/2017.
 */

public class FeedbackActivity extends BaseActivity{
    @Override
    public void onInitData(Bundle savedInstanceState) {

    }

    @Override
    public void onInitView() {
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("意见反馈");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_feedback;
    }
}
