package com.king.reading.module.vp.presenter;

import android.support.annotation.NonNull;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.king.reading.common.utils.ClickFliter;
import com.king.reading.common.utils.VerifyUtils;
import com.king.reading.data.entities.UserEntity;
import com.king.reading.data.repository.ResRepository;
import com.king.reading.data.repository.UserRepository;
import com.king.reading.encyption.MD5Utils;
import com.king.reading.exception.BusinessException;
import com.king.reading.mod.ModConst;
import com.king.reading.module.vp.view.LoginView;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * Created by hu.yang on 2017/5/10.
 */

public class LoginPresenter implements Presenter<LoginView>{
    private LoginView view;
    private UserRepository userRepository;
    private ResRepository resRepository;

    @Inject
    public LoginPresenter(UserRepository userRepository, ResRepository resRepository) {
        this.userRepository = userRepository;
        this.resRepository = resRepository;
    }

    @Override
    public void setView(LoginView view) {
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

    public void login(String userName, String passWord){

        if (!ClickFliter.canClick())
            return;

        if (!VerifyUtils.verifyPwd(passWord)) return;

        if (!VerifyUtils.verifyPhoneNumber(userName)) return;

        userRepository.login(userName, MD5Utils.getMD5ofStr(passWord)).subscribe(new Consumer<UserEntity>() {
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
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                if (throwable instanceof BusinessException){
                    BusinessException exception = BusinessException.class.cast(throwable);
                    int errCode = exception.getErrCode();
                    switch (errCode){
                        case ModConst.ERR_ACCOUNT_PASSWD:
                            view.toast("账号或密码错误");
                            break;
                        case ModConst.ERR_ACCOUNT_ABNORMAL:
                            view.toast("用户名不存在或已被禁用");
                            break;
                        case ModConst.ERR_ACCOUNTCENTER_NIL:
                            view.toast("账户不存在");
                        default:
                            Logger.e(throwable.getMessage());
                    }
                } else
                    Logger.e(throwable.getMessage());
            }
        });
    }

    public BiFunction getEventWatcher(){
        return new BiFunction<TextViewAfterTextChangeEvent, TextViewAfterTextChangeEvent, Boolean>() {
            @Override
            public Boolean apply(@NonNull TextViewAfterTextChangeEvent userName, @NonNull TextViewAfterTextChangeEvent password) throws Exception {
                String u = userName.editable().toString();
                String p = password.editable().toString();

                view.haveInputPhone(u.length() > 0);
                view.haveInputPwd(p.length() > 0);

                if( u.length() != 11)
                    return false;
                if ( p.length() < 6)
                    return false;

                return true;
            }
        };
    }
}
