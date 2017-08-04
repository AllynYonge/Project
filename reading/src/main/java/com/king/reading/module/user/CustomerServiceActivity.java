package com.king.reading.module.user;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.common.utils.PermissionsChecker;

import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by AllynYonge on 21/06/2017.
 */

@RuntimePermissions
@Route(path = C.ROUTER_CUSTOMER)
public class CustomerServiceActivity extends BaseActivity{

    @Override
    public void onInitData(Bundle savedInstanceState) {

    }

    @Override
    public void onInitView() {
        TitleBuilder.build(this).setLeftIcon(R.mipmap.ic_back).setLeftIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).setCenterTitle("客服服务")
        .init();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_customer_service;
    }

    @OnClick(R.id.ll_customer_wxCompanyNum)
    public void wxCompanyNum(View view){
        new MaterialDialog.Builder(this)
                .content("复制成功，在微信粘贴搜索点读宝公众号关注即可")
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        ClipboardUtils.copyText("Kingsunsoft_DDB");
                    }
                })
                .show();

    }
    @OnClick(R.id.ll_customer_wxPersonNum)
    public void wxPersonNum(View view){
        new MaterialDialog.Builder(this)
                .content("复制成功，在微信粘贴搜索个人号关注即可")
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        ClipboardUtils.copyText("jtyddb");
                    }
                })
                .show();
    }
    @OnClick(R.id.ll_customer_qqNum)
    public void qqNum(View view){
        new MaterialDialog.Builder(this)
                .content("复制成功，在QQ粘贴搜索QQ群即可")
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        ClipboardUtils.copyText("604074830");
                    }
                })
                .show();
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    @OnClick(R.id.ll_customer_phoneNum)
    public void phoneNum(View view){
        if (PermissionsChecker.lacksPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)){
            CustomerServiceActivityPermissionsDispatcher.phoneNumWithCheck(this, view);
        } else {
            PhoneUtils.call("4001118180");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CustomerServiceActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        if (grantResults[0] == 0){
            PhoneUtils.call("4001118180");
        }
    }
}
