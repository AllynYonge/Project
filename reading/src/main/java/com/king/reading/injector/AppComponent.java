package com.king.reading.injector;


import com.king.reading.Navigation;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.data.repository.OtherRepository;
import com.king.reading.data.repository.ResRepository;
import com.king.reading.data.repository.UserRepository;
import com.king.reading.domain.cache.PageCache;
import com.king.reading.domain.cache.UserCache;
import com.king.reading.encyption.glide.EncryptionFileToStreamDecoder;
import com.king.reading.injector.components.UserComponent;
import com.king.reading.injector.modules.NetModule;
import com.king.reading.module.SplashActivity;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import io.reactivex.Scheduler;

/**
 * Created by hu.yang on 2017/4/25.
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {
    UserComponent plus();
    void inject(BaseActivity baseActivity);
    void inject(SplashActivity splashActivity);
    Navigation provideNavigation();
    OtherRepository provideOtherRepo();
    UserRepository provideUserRepo();
    ResRepository provideResRepo();

    //glide
    EncryptionFileToStreamDecoder getCacheDecoder();

    //Scheduler
    @Named("io")
    Scheduler getIoScheduler();
    @Named("main")
    Scheduler getMainScheduler();

    //Cache
    UserCache getUserCache();
    PageCache getPageCache();
}
