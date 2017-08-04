package com.king.reading.module.vp.view;

/**
 * Created by AllynYonge on 12/07/2017.
 */

public interface RegisterView {
    void responseVerityCode(String verityCode);

    void haveInputPwd(boolean isWrite);
    void haveInputVerity(boolean isWrite);
    void haveInputPhone(boolean isWrite);

    void routerMainAct(long usingBook);
    void routerUploadAvatar();
    void routerCompletionProfileAct();
    void routerSelectVersionAct();
}
