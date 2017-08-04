package com.king.reading.injector.components;


import com.king.reading.injector.modules.UserModule;
import com.king.reading.module.MainActivity;
import com.king.reading.module.learn.ListenActivity;
import com.king.reading.module.learn.roleplay.RolePlayingChoiceActivity;
import com.king.reading.module.read.ReadDetailActivity;
import com.king.reading.module.user.FindPwdActivity;
import com.king.reading.module.user.LoginActivity;
import com.king.reading.module.user.PayFeaturesActivity;
import com.king.reading.module.user.RegisterActivity;
import com.king.reading.module.user.SelectClassActivity;
import com.king.reading.module.user.SelectVersionActivity;
import com.king.reading.module.user.SettingNewPwdActivity;
import com.king.reading.module.user.SupplementProfileActivity;
import com.king.reading.module.user.UpdateVersionActivity;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Created by hu.yang on 2017/5/15.
 */

@Singleton
@Subcomponent(modules = UserModule.class)
public interface UserComponent{
    void inject(LoginActivity loginActivity);
    void inject(MainActivity activity);
    void inject(ReadDetailActivity readDetailActivity);
    void inject(RegisterActivity activity);
    void inject(FindPwdActivity activity);
    void inject(SettingNewPwdActivity activity);
    void inject(SupplementProfileActivity activity);
    void inject(SelectVersionActivity activity);
    void inject(SelectClassActivity activity);
    void inject(UpdateVersionActivity activity);
    void inject(ListenActivity listenActivity);
    void inject(RolePlayingChoiceActivity activity);
    void inject(PayFeaturesActivity activity);
}
