package com.king.reading.module;


import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.king.reading.AppInfo;
import com.king.reading.Navigation;
import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.data.entities.UserEntity;
import com.king.reading.data.repository.UserRepository;
import com.king.reading.exception.handler.SimpleCrashHandler;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.vov.vitamio.Vitamio;

public class SplashActivity extends Activity {
    @Inject
    Navigation navigation;
    @Inject
    UserRepository userRepository;

    private Handler myHandler = new Handler();
    private Runnable mLoadingRunnable = new Runnable() {

        @Override
        public void run() {
            UserEntity user = userRepository.getUser();
            if (EmptyUtils.isNotEmpty(user)) {
                if (user.nickName.isEmpty() ||
                        user.schoolName.isEmpty() || user.className.isEmpty()) {
                    navigation.routerCompletionProfileAct();
                } else if (user.firstLogin) {
                    navigation.routerUploadAvatarAct();
                } else if (user.usingBook == 0) {
                    navigation.routerSelectVersionAct();
                } else {
                    navigation.routerMainAct(user.usingBook);
                }
            } else {
                navigation.routerLoginAct();
            }
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSimpleCrashHandler();
        SysApplication.getApplication().getAppComponent().inject(this);
        setContentView(R.layout.activity_splash);
        ImageView splashView = (ImageView) findViewById(R.id.image_splash);
        Glide.with(this).fromResource().load(R.mipmap.bg_splash).animate(android.support.design.R.anim.abc_fade_in).into(splashView);
        getWindow().getDecorView().post(new Runnable() {

            @Override
            public void run() {
                //base information init
                AppInfo.getInstance().init(getApplicationContext());
                Vitamio.isInitialized(getApplicationContext());
                myHandler.postDelayed(mLoadingRunnable, 2000);
            }
        });
    }

    private void initSimpleCrashHandler() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        if (aBoolean) {
                            SimpleCrashHandler.createBuilder(SysApplication.getApplication())
                                    .setIsWriteLog(true)
                                    .setAppCrashPromptString(R.string.app_crash)
                                    .create().init();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
