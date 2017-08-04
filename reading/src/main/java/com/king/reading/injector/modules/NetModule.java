package com.king.reading.injector.modules;

import com.king.reading.C;
import com.king.reading.net.Api;
import com.king.reading.net.converter.TarsConverterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hu.yang on 2017/5/12.
 */

@Module
public class NetModule {

    @Named("tars")
    @Provides
    @Singleton
    Api provideLoginApi(){
        OkHttpClient okClient = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

        return new Retrofit.Builder()
                .baseUrl(C.API_BASE_URL)
                .addConverterFactory(new TarsConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okClient)
                .build()
                .create(Api.class);
    }

    @Named("gson")
    @Provides
    @Singleton
    Api provideApi(){
        OkHttpClient okClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.11.28:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okClient)
                .build();

        return retrofit.create(Api.class);
    }

}
