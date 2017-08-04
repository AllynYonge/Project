package com.king.reading.module.vp.view;

/**
 * Created by AllynYonge on 12/07/2017.
 */

public interface LoginView extends BaseView{
    void showProgress();
    void dismissProgress();
    void toast(String message);

    void haveInputPhone(boolean isWrite);
    void haveInputPwd(boolean isWrite);

    void routerMainAct(long usingBook);
    void routerUploadAvatar();
    void routerCompletionProfileAct();
    void routerSelectVersionAct();
}
