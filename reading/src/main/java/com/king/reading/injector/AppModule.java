/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.king.reading.injector;

import android.app.Application;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperResourceDecoder;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperStreamResourceDecoder;
import com.bumptech.glide.provider.DataLoadProvider;
import com.bumptech.glide.provider.DataLoadProviderRegistry;
import com.king.reading.Navigation;
import com.king.reading.SysApplication;
import com.king.reading.encyption.glide.EncryptionFileToStreamDecoder;

import java.io.InputStream;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class AppModule {

    private final Application mSysApplication;

    public AppModule(Application sysApplication) {
        this.mSysApplication = sysApplication;
    }

    @Singleton @Provides
    protected Application provideSystemApplication(){
        return mSysApplication;
    }

    @Singleton @Provides
    protected Navigation provideNavigation(){
        return new Navigation();
    }

    @Singleton @Provides @Named("io")
    protected Scheduler provideIOScheduler(){
        return Schedulers.io();
    }

    @Singleton @Provides @Named("main")
    protected Scheduler provideMainScheduler(){
        return AndroidSchedulers.mainThread();
    }

    @Singleton @Provides
    protected EncryptionFileToStreamDecoder provideCacheDecoder(){
        DataLoadProviderRegistry dataLoadProviderRegistry = SysApplication.class.cast(mSysApplication).getDataLoadProviderRegistry();
        DataLoadProvider<ImageVideoWrapper, Bitmap> bitmapProvider = dataLoadProviderRegistry.get(ImageVideoWrapper.class, Bitmap.class);
        DataLoadProvider<InputStream, GifDrawable> GifProvider = dataLoadProviderRegistry.get(InputStream.class, GifDrawable.class);
        GifBitmapWrapperResourceDecoder decoder = new GifBitmapWrapperResourceDecoder(bitmapProvider.getSourceDecoder(), GifProvider.getSourceDecoder(), Glide.get(mSysApplication).getBitmapPool());
        return new EncryptionFileToStreamDecoder(new GifBitmapWrapperStreamResourceDecoder(decoder));
    }
}
