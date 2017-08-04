package com.king.reading.module.user;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.base.activity.BaseActivity;

/**
 * Created by AllynYonge on 20/06/2017.
 */

@Route(path = C.ROUTER_SETTING)
public class SettingActivity extends BaseActivity{
    @Override
    public void onInitData(Bundle savedInstanceState) {

    }

    @Override
    public void onInitView() {
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("设置");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_setting;
    }
}
