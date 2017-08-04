package com.king.reading.module.user;

import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.king.reading.C;
import com.king.reading.Navigation;
import com.king.reading.R;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.common.utils.VerifyUtils;
import com.king.reading.data.repository.UserRepository;
import com.king.reading.encyption.MD5Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by AllynYonge on 21/06/2017.
 */

@Route(path = C.ROUTER_SETNEWPWD)
public class SettingNewPwdActivity extends BaseActivity{
    @Inject Navigation navigation;
    @Inject UserRepository userRepository;
    @BindView(R.id.edit_setNewPwd_pwd) EditText pwd;
    @BindView(R.id.chk_setNewPwd_see) AppCompatCheckBox seePwd;
    @BindView(R.id.tv_setNewPwd_next) TextView next;
    @BindView(R.id.tv_setNewPwd_pwdIcon) TextView pwdIcon;
    @Autowired
    public String verityCode;
    @Autowired
    public String phoneNumber;
    @Override
    public void onInitData(Bundle savedInstanceState) {
        getAppComponent().plus().inject(this);
        ARouter.getInstance().inject(this);
    }

    @Override
    public void onInitView() {
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("找回密码");
        RxTextView.afterTextChangeEvents(pwd).subscribe(new Consumer<TextViewAfterTextChangeEvent>() {
            @Override
            public void accept(@NonNull TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) throws Exception {
                pwdIcon.setEnabled(textViewAfterTextChangeEvent.editable().length() > 0);
            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_settpwd;
    }

    @OnClick(R.id.ll_setNewPwd_see)
    public void seePwd(View view) {
        seePwd.setChecked(!seePwd.isChecked());
        if (seePwd.isChecked())
            pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    }

    @OnTextChanged(R.id.edit_setNewPwd_pwd)
    public void verityPwd(CharSequence charSequence, int start, int before, int count){
        if (charSequence.length() <= 20 && charSequence.length() >=6)
            next.setEnabled(true);
        else
            next.setEnabled(false);
    }

    @OnClick(R.id.tv_setNewPwd_next)
    public void next(View view){
        if (!VerifyUtils.verifyPwd(pwd.getText().toString().trim()))
            return;

        if (!VerifyUtils.verifyPhoneNumber(phoneNumber))
            return;

        userRepository.resetPassword(phoneNumber, MD5Utils.getMD5ofStr(pwd.getText().toString().trim()), verityCode).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                navigation.routerLoginAct();
            }
        });

    }
}
