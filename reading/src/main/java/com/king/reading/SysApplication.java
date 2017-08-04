package com.king.reading;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.alipay.sdk.app.EnvUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.provider.DataLoadProviderRegistry;
import com.king.reading.common.utils.DebugUtil;
import com.king.reading.encyption.glide.EncryptionImageLoader;
import com.king.reading.injector.AppComponent;
import com.king.reading.injector.AppModule;
import com.king.reading.injector.DaggerAppComponent;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tencent.smtt.sdk.QbSdk;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;


/**
* @ClassName: SysApplication 
* @Description: TODO(管理后台所有Activity) 
* @author LXL 
* @date 2016-1-6 上午9:18:59 
*  
*/ 
public class SysApplication extends Application {

    private static SysApplication app;
    private List<Activity> mList = new ArrayList();
    private AppComponent mAppComponent;
    private static Handler mHandler = new Handler();
    private DataLoadProviderRegistry dataLoadProviderRegistry;

    @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        FlowManager.init(this);
        app = this;
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        initRouter();
        ShareSDK.initSDK(this);
        /*SimpleCrashHandler.createBuilder(this)
                .setIsWriteLog(true)
                .setAppCrashPromptString(R.string.app_crash)
                .create().init();*/
        if (!DebugUtil.isDebuggable()){
            initX5();
        }
        initLog();
        initCloudChannel(this);
        initGlide();
        Utils.init(this);
    }

    public static SysApplication getApplication(){
        return app;
    }

    public DataLoadProviderRegistry getDataLoadProviderRegistry() {
        return dataLoadProviderRegistry;
    }

    public static Handler getMainThreadHandler() {
        return mHandler;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    /**
     * 初始化Glide配置，因为要加解密
     */
    private void initGlide() {
        Glide.get(this).register(GlideUrl.class, InputStream.class, new EncryptionImageLoader.Factory());
        try {
            Field dataLoadProviderRegistryField = Glide.class.getDeclaredField("dataLoadProviderRegistry");
            dataLoadProviderRegistryField.setAccessible(true);
            dataLoadProviderRegistry = (DataLoadProviderRegistry) dataLoadProviderRegistryField.get(Glide.get(this));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化x5内核
     */
    private void initX5(){
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Logger.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }

    /**
     * 初始化日志管理器
     */
    public void initLog(){
        //Debug init
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    /**
     * 初始化路由系统
     */
    private void initRouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(this);
    }

    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(final Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Logger.d("init cloudchannel success");
                // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
                MiPushRegister.register(applicationContext, "2882303761517455982", "5811745567982");
                // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
                HuaWeiRegister.register(applicationContext);
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Logger.d("init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

    //杀进程
    @Override
	public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

}
 