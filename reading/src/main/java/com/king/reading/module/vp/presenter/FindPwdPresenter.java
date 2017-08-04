package com.king.reading.module.vp.presenter;

import android.support.annotation.NonNull;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.king.reading.common.utils.Check;
import com.king.reading.data.repository.UserRepository;
import com.king.reading.ddb.GetVerifyCodeResponse;
import com.king.reading.module.vp.view.FindPwdView;

import javax.inject.Inject;

import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * Created by AllynYonge on 12/07/2017.
 */

public class FindPwdPresenter implements Presenter<FindPwdView> {
    private FindPwdView view;
    @Inject UserRepository userRepository;

    @Inject
    public FindPwdPresenter() {
    }

    @Override
    public void setView(FindPwdView view) {
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

    public void getVerityCode(String phone, Consumer<Throwable> consumer) {
        userRepository.getVerityCode(phone).subscribe(new Consumer<GetVerifyCodeResponse>() {
            @Override
            public void accept(@NonNull GetVerifyCodeResponse response) throws Exception {
                view.responseVerityCode(response.getReserved());
            }
        }, consumer);
    }

    @NonNull
    public BiFunction<TextViewAfterTextChangeEvent, TextViewAfterTextChangeEvent, Boolean> getTextWatcher() {
        return new BiFunction<TextViewAfterTextChangeEvent, TextViewAfterTextChangeEvent, Boolean>() {
            @Override
            public Boolean apply(@io.reactivex.annotations.NonNull TextViewAfterTextChangeEvent phone, @io.reactivex.annotations.NonNull TextViewAfterTextChangeEvent verifyCode) throws Exception {
                String phoneText = phone.editable().toString();
                String verifyText = verifyCode.editable().toString();
                view.haveInputPhone(phoneText.length() > 0);
                view.haveInputVerity(verifyText.length() > 0);
                if( phoneText.length() != 11)
                    return false;
                if (Check.isEmpty(verifyText))
                    return false;
                return true;
            }
        };
    }
}
