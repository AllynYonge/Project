package com.king.reading.module.user;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.king.reading.C;
import com.king.reading.R;

/**
 * Created by AllynYonge on 11/07/2017.
 */

@Route(path = C.ROUTER_UPDATEVERSION)
public class UpdateVersionActivity extends SelectClassActivity {
    @Autowired
    public long bookId;

    @Override
    public void onInitData(Bundle savedInstanceState) {
        getAppComponent().plus().inject(this);
        ARouter.getInstance().inject(this);
    }
    @Override
    public void onInitView() {
        areaName = userRepository.getAreaNameForId(bookId);
        super.onInitView();
        setLeftImageIcon(R.mipmap.ic_back);
        showPrompt();
    }
}
