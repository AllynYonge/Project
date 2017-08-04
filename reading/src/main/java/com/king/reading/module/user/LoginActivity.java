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
import com.king.reading.common.utils.ToastUtils;
import com.king.reading.module.vp.presenter.LoginPresenter;
import com.king.reading.module.vp.view.LoginView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by AllynYonge on 20/06/2017.
 */

@Route(path = C.ROUTER_LOGIN)
public class LoginActivity extends BaseActivity implements LoginView {
    @BindView(R.id.tv_login) TextView mLogin;
    @BindView(R.id.tv_login_phone) EditText mUserName;
    @BindView(R.id.tv_login_pwd) EditText mPassword;
    @BindView(R.id.tv_login_pwd_ic) TextView pwdIcon;
    @BindView(R.id.tv_login_phone_ic) TextView phoneIcon;

    @Inject LoginPresenter presenter;
    @Inject Navigation navigation;

    @Override
    public void onInitData(Bundle savedInstanceState) {
        getAppComponent().plus().inject(this);
    }

    @Override
    public void onInitView() {
        setCenterTitle("登录");
        presenter.setView(this);
        Observable.combineLatest(RxTextView.afterTextChangeEvents(mUserName),
                RxTextView.afterTextChangeEvents(mPassword), presenter.getEventWatcher())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        mLogin.setEnabled(aBoolean);
                    }
                });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.tv_login_findPwd)
    public void findPwd(View view) {
        navigation.routerFindPwdAct();
}

    @OnClick(R.id.tv_login_register)
    public void register(View view) {
        navigation.routerRegisterAct();
    }

    @OnClick(R.id.tv_login)
    public void login(View view) {
        presenter.login(mUserName.getText().toString().trim(), mPassword.getText().toString().trim());
    }

    @Override
    public void showProgress() {
        showProgressDialog("正在登录......");
    }

    @Override
    public void dismissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void toast(String message) {
        ToastUtils.show(message);
    }

    @Override
    public void haveInputPhone(boolean isWrite) {
        phoneIcon.setEnabled(isWrite);
    }

    @Override
    public void haveInputPwd(boolean isWrite) {
        pwdIcon.setEnabled(isWrite);
    }

    @Override
    public void routerMainAct(long usingBook) {
        navigation.routerMainAct(usingBook);
    }

    @Override
    public void routerUploadAvatar() {
        navigation.routerLoginAct();
        finish();
    }

    @Override
    public void routerCompletionProfileAct() {
        navigation.routerCompletionProfileAct();
        finish();
    }

    @Override
    public void routerSelectVersionAct() {
        navigation.routerSelectVersionAct();
        finish();
    }
}
