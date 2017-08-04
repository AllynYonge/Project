package com.king.reading.module.vp.presenter;

import android.util.Log;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.king.reading.common.utils.Check;
import com.king.reading.common.utils.ClickFliter;
import com.king.reading.common.utils.ToastUtils;
import com.king.reading.common.utils.VerifyUtils;
import com.king.reading.data.entities.UserEntity;
import com.king.reading.data.repository.UserRepository;
import com.king.reading.ddb.GetVerifyCodeResponse;
import com.king.reading.encyption.MD5Utils;
import com.king.reading.module.vp.view.RegisterView;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

/**
 * Created by AllynYonge on 12/07/2017.
 */

public class RegisterPresenter implements Presenter<RegisterView>{
    private UserRepository userRepository;
    private RegisterView view;
    @Inject
    public RegisterPresenter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void setView(RegisterView view) {
        this.view = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void register(String userName, String pwd, String verityCode) {
        if (!ClickFliter.canClick())
            return;
        if (!VerifyUtils.verifyPhoneNumber(userName)) return;
        if (!VerifyUtils.verifyPwd(pwd)) return;

        if (Check.isEmpty(verityCode)) {
            ToastUtils.show("请输入验证码");
            return;
        }

        userRepository.register(userName, MD5Utils.getMD5ofStr(pwd), verityCode).subscribe(new Consumer<UserEntity>() {
            @Override
            public void accept(@NonNull UserEntity user) throws Exception {
                if (user.nickName.isEmpty() ||
                        user.schoolName.isEmpty() || user.className.isEmpty()) {
                    view.routerCompletionProfileAct();
                } else if (user.firstLogin){
                    view.routerUploadAvatar();
                } else if (user.usingBook == 0){
                    view.routerSelectVersionAct();
                } else {
                    view.routerMainAct(user.usingBook);
                }
            }
        });
    }

    public void getVerityCode(String phone, Consumer<Throwable> getCodeView) {
        userRepository.getVerityCode(phone).subscribe(new Consumer<GetVerifyCodeResponse>() {
            @Override
            public void accept(@NonNull GetVerifyCodeResponse isSuccess) throws Exception {
                Log.d("RegisterPresenter", "isSuccess:" + isSuccess);
                view.responseVerityCode(isSuccess.getReserved());
            }
        }, getCodeView);
    }

    public Function3<? super TextViewAfterTextChangeEvent, ? super TextViewAfterTextChangeEvent, ? super TextViewAfterTextChangeEvent, Boolean> getTextWatcher() {
        return new Function3<TextViewAfterTextChangeEvent, TextViewAfterTextChangeEvent, TextViewAfterTextChangeEvent, Boolean>() {
            @Override
            public Boolean apply(@io.reactivex.annotations.NonNull TextViewAfterTextChangeEvent phone, @io.reactivex.annotations.NonNull TextViewAfterTextChangeEvent pwd, @io.reactivex.annotations.NonNull TextViewAfterTextChangeEvent verifyCode) throws Exception {
                String phoneText = phone.editable().toString();
                String pwdText = pwd.editable().toString();
                String verifyText = verifyCode.editable().toString();

                view.haveInputPhone(phoneText.length() > 0);
                view.haveInputVerity(verifyText.length() > 0);
                view.haveInputPwd(pwdText.length() > 0);

                if( phoneText.length() != 11)
                    return false;
                if ( pwdText.length() < 6)
                    return false;
                if (Check.isEmpty(verifyText))
                    return false;
                return true;
            }
        };
    }
}
