package com.king.reading.module.user;

import android.os.Bundle;

import com.king.reading.R;
import com.king.reading.base.activity.BaseActivity;

/**
 * Created by AllynYonge on 21/06/2017.
 */

public class ChangePwdActivity extends BaseActivity{
    @Override
    public void onInitData(Bundle savedInstanceState) {

    }

    @Override
    public void onInitView() {
        setCenterTitle("修改密码");
        setLeftImageIcon(R.mipmap.ic_back);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_changepwd;
    }
}
