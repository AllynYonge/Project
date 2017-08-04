package com.king.reading.module.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.EmptyUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.king.reading.C;
import com.king.reading.Navigation;
import com.king.reading.R;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.common.utils.SmsCodeHandler;
import com.king.reading.module.vp.presenter.RegisterPresenter;
import com.king.reading.module.vp.view.RegisterView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by AllynYonge on 21/06/2017.
 */

@Route(path = C.ROUTER_REGISTER)
public class RegisterActivity extends BaseActivity implements RegisterView {
    @Inject RegisterPresenter presenter;
    @Inject Navigation navigation;
    @BindView(R.id.edit_register_phone) EditText phoneNumber;
    @BindView(R.id.edit_register_pwd) EditText pwd;
    @BindView(R.id.edit_register_verityCode) EditText verityCode;
    @BindView(R.id.tv_register_getCode) TextView getCode;
    @BindView(R.id.tv_register) TextView register;
    @BindView(R.id.chk_register_see) AppCompatCheckBox seePwd;
    @BindView(R.id.tv_register_phone_icon) TextView phoneIcon;
    @BindView(R.id.tv_register_pwd_icon) TextView pwdIcon;
    @BindView(R.id.tv_register_verity_icon) TextView verityIcon;
    @BindView(R.id.chk_register_selected) CompoundButton agreeView;
    private SmsCodeHandler smsCodeHandler;

    @Override
    public void onInitData(Bundle savedInstanceState) {
    }

    @Override
    public void onInitView() {
        getAppComponent().plus().inject(this);
        presenter.setView(this);
        smsCodeHandler = new SmsCodeHandler(this, getCode);
        Observable.combineLatest(RxTextView.afterTextChangeEvents(phoneNumber), RxTextView.afterTextChangeEvents(pwd),
                RxTextView.afterTextChangeEvents(verityCode), presenter.getTextWatcher())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Boolean isEnable) throws Exception {
                        register.setEnabled(isEnable && agreeView.isChecked());
                    }
                });
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("快速注册");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.ll_register_see)
    public void seePwd(View view) {
        seePwd.setChecked(!seePwd.isChecked());
        if (seePwd.isChecked())
            pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    }

    @OnClick(R.id.tv_register_getCode)
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

    @OnClick(R.id.tv_register)
    public void register(View view) {
        presenter.register(phoneNumber.getText().toString().trim(),
                pwd.getText().toString().trim(),
                verityCode.getText().toString().trim());
    }

    @OnClick(R.id.tv_register_agreement)
    public void agreement(View view){

    }

    @OnCheckedChanged(R.id.chk_register_selected)
    public void agree(CompoundButton view, boolean isCheck){
        if( phoneNumber.getText().toString().length() != 11){
            register.setEnabled(false);
            return;
        }

        if ( pwd.getText().toString().length() < 6){
            register.setEnabled(false);
            return;
        }

        if (EmptyUtils.isEmpty(verityCode.getText().toString())){
            register.setEnabled(false);
            return;
        }

        register.setEnabled(isCheck);
    }

    @Override
    public void responseVerityCode(String verityCode) {
        this.verityCode.setText(verityCode);
    }

    @Override
    public void haveInputPwd(boolean isWrite) {
        pwdIcon.setEnabled(isWrite);
    }

    @Override
    public void haveInputVerity(boolean isWrite) {
        verityIcon.setEnabled(isWrite);
    }

    @Override
    public void haveInputPhone(boolean isWrite) {
        phoneIcon.setEnabled(isWrite);
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
