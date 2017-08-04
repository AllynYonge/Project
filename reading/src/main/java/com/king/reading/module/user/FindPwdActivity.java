package com.king.reading.module.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.king.reading.C;
import com.king.reading.Navigation;
import com.king.reading.R;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.common.utils.ClickFliter;
import com.king.reading.common.utils.SmsCodeHandler;
import com.king.reading.module.vp.presenter.FindPwdPresenter;
import com.king.reading.module.vp.view.FindPwdView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by AllynYonge on 21/06/2017.
 */

@Route(path = C.ROUTER_FINDPWD)
public class FindPwdActivity extends BaseActivity implements FindPwdView{
    @Inject FindPwdPresenter presenter;
    @Inject Navigation navigation;
    @BindView(R.id.tv_findPwd_getCode) TextView getCode;
    @BindView(R.id.edit_findPwd_phoneNumber) EditText phoneNumber;
    @BindView(R.id.edit_findPwd_verifyCode) EditText verifyCode;
    @BindView(R.id.tv_findPwd_next) TextView next;
    @BindView(R.id.tv_findPwd_verify_icon) TextView verityIcon;
    @BindView(R.id.tv_findPwd_phone_icon) TextView phoneIcon;
    private SmsCodeHandler smsCodeHandler;
    @Override
    public void onInitData(Bundle savedInstanceState) {
        getAppComponent().plus().inject(this);
    }

    @Override
    public void onInitView() {
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("找回密码");
        presenter.setView(this);
        smsCodeHandler = new SmsCodeHandler(this, getCode);
        Observable.combineLatest(RxTextView.afterTextChangeEvents(phoneNumber)
                , RxTextView.afterTextChangeEvents(verifyCode), presenter.getTextWatcher())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {
                        next.setEnabled(aBoolean);
                    }
                });
    }



    @OnClick(R.id.tv_findPwd_getCode)
    public void getVerityCode(View view) {
        smsCodeHandler.sendEmptyMessage(SmsCodeHandler.CHANGETIME);
        presenter.getVerityCode(phoneNumber.getText().toString().trim(), new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                smsCodeHandler.removeMessages(SmsCodeHandler.CHANGETIME);
                smsCodeHandler.sendEmptyMessageDelayed(SmsCodeHandler.STOP, 500);
            }
        });
    }

    @OnClick(R.id.tv_findPwd_next)
    public void next(View view){
        if (ClickFliter.canClick())
            navigation.routerResetPwdAct(phoneNumber.getText().toString().trim(), verifyCode.getText().toString().trim());
    }

    @Override
    public int getContentView() {
        return R.layout.activity_findpwd;
    }

    @Override
    public void responseVerityCode(String verityCodeText) {
        verifyCode.setText(verityCodeText);
    }

    @Override
    public void haveInputPhone(boolean isWrite) {
        phoneIcon.setEnabled(isWrite);
    }

    @Override
    public void haveInputVerity(boolean isWrite) {
        verityIcon.setEnabled(isWrite);
    }
}
