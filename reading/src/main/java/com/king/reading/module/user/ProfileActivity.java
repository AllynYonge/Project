package com.king.reading.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.common.utils.AppUtils;
import com.king.reading.widget.drawer.view.BezelImageView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hu.yang on 2017/6/19.
 */

public class ProfileActivity extends BaseActivity {
    @BindView(R.id.image_profile_avatar)
    BezelImageView mImageProfileAvatar;

    @Override
    public void onInitData(Bundle savedInstanceState) {
        AppUtils.setHeadImage(mImageProfileAvatar,this);
    }

    @Override
    public void onInitView() {
        setCenterTitle("个人资料");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_profile;
    }

    @OnClick(R.id.image_profile_avatar)
    public void supplementProfile(View v){
        startActivity(new Intent(this,SupplementProfileActivity.class));
    }

    @Override
    public boolean hasEventBus() {
        return true;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Integer event) {
        //头像上传成功的通知
        if (event== C.EADIMAGE_IS_UPDATE){
            AppUtils.setHeadImage(mImageProfileAvatar,this);
        }
    }
}
