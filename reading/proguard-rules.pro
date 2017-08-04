    # Add project specific ProGuard rules here.
    # By default, the flags in this file are appended to flags specified
    # in E:\Job\sdk/tools/proguard/proguard-android.txt
    # You can edit the include path and order by changing the proguardFiles
    # directive in build.gradle.
    #
    # For more details, see
    #   http://developer.android.com/guide/developing/tools/proguard.html

    # Add any project specific keep options here:

    # If your project uses WebView with JS, uncomment the following
    # and specify the fully qualified class name to the JavaScript interface
    # class:
    #-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    #   public *;
    #}

    # Uncomment this to preserve the line number information for
    # debugging stack traces.
    #-keepattributes SourceFile,LineNumberTable

    # If you keep the line number information, uncomment this to
    # hide the original source file name.
    #-renamesourcefileattribute SourceFile
    # To enable ProGuard in your project, edit project.properties
    # to define the proguard.config property as described in that file.
    #
    # Add project specific ProGuard rules here.
    # By default, the flags in this file are appended to flags specified
    # in ${sdk.dir}/tools/proguard/proguard-android.txt
    # You can edit the include path and order by changing the ProGuard
    # include property in project.properties.
    #
    # For more details, see
    #   http://developer.android.com/guide/developing/tools/proguard.html

    # Add any project specific keep options here:

    # If your project uses WebView with JS, uncomment the following
    # and specify the fully qualified class name to the JavaScript interface
    # class:
    #-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    #   public *;
    #}

    # 1. 代码中使用了反射，如一些ORM框架的使用
    #           需要保证类名 方法不变, 不然混淆后, 就反射不了
    # 2. 使用GSON、fastjson等JSON解析框架所生成的对象类
    #           生成的bean实体对象,内部大多是通过反射来生成, 不能混淆
    # 3. 引用了第三方开源框架或继承第三方SDK，如开源的okhttp网络访问框架，百度定位SDK等
    #           在这些第三库的文档中 一班会给出 相应的 混淆规则, 复制过来即可
    # 4. 有用到WEBView的JS调用接口
    #           没真么用过这块, 不是很熟, 网上那个看到的
    # 5. 继承了Serializable接口的类
    #           在反序列画的时候, 需要正确的类名等, 在Android 中大多是实现 Parcelable来序列化的
    -keepattributes InnerClasses
    -dontoptimize

    ######################### 第三方平台 #########################

    ##支付
    #pay_library
    -dontwarn io.github.mayubao.pay_library.**
    -keep class io.github.mayubao.pay_library.** {*;}

    #wechat pay
    -dontwarn com.tencent.**
    -keep class com.tencent.** {*;}


    #alipay
    -dontwarn com.alipay.**
    -keep class com.alipay.** {*;}

    -dontwarn  com.ta.utdid2.**
    -keep class com.ta.utdid2.** {*;}

    -dontwarn  com.ut.device.**
    -keep class com.ut.device.** {*;}

    -dontwarn  org.json.alipay.**

    -keep public class **.R$*{
       public static final int *;
    }

    #umeng
    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }

    -keep class com.umeng.onlineconfig.OnlineConfigAgent {
            public <fields>;
            public <methods>;
    }

    -keep class com.umeng.onlineconfig.OnlineConfigLog {
            public <fields>;
            public <methods>;
    }

    -keep interface com.umeng.onlineconfig.UmengOnlineConfigureListener {
            public <methods>;
    }

    -dontwarn com.umeng.fb.**
    -dontwarn com.iflytek.autoupdate.**

    #社会化分享
    -dontshrink
    -dontoptimize
    -dontwarn com.google.android.maps.**
    -dontwarn android.webkit.WebView
    -dontwarn com.umeng.**
    #-dontwarn com.tencent.weibo.sdk.**
    -dontwarn com.facebook.**
    -keep public class javax.**
    -keep public class android.webkit.**
    -dontwarn android.support.v4.**
    -keep enum com.facebook.**
    -keepattributes Exceptions,InnerClasses,Signature
    -keepattributes SourceFile,LineNumberTable

    -keep public interface com.facebook.**
    #-keep public interface com.tencent.**
    -keep public interface com.umeng.socialize.**
    -keep public interface com.umeng.socialize.sensor.**
    -keep public interface com.umeng.scrshot.**

    -keep public class com.umeng.socialize.* {*;}


    -keep class com.facebook.**
    -keep class com.facebook.** { *; }
    -keep class com.umeng.scrshot.**
    -keep class com.umeng.socialize.sensor.**
    -keep class com.umeng.socialize.handler.**
    -keep class com.umeng.socialize.handler.*
    #-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
    #-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

    -keep class im.yixin.sdk.api.YXMessage {*;}
    -keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}

    -dontwarn twitter4j.**
    -keep class twitter4j.** { *; }

    -keep public class com.umeng.soexample.R$*{
     public static final int *;
    }
    -keep public class com.umeng.soexample.R$*{
     public static final int *;
    }
    #-keep class com.tencent.open.TDialog$*
    #-keep class com.tencent.open.TDialog$* {*;}
    #-keep class com.tencent.open.PKDialog
    #-keep class com.tencent.open.PKDialog {*;}
    #-keep class com.tencent.open.PKDialog$*
    #-keep class com.tencent.open.PKDialog$* {*;}

    -keep class com.sina.** {*;}
    -dontwarn com.sina.**
    -keep class  com.alipay.share.sdk.** {
    *;
    }
    -keepnames class * implements android.os.Parcelable {
     public static final ** CREATOR;
    }

    -keep class com.linkedin.** { *; }
    -keepattributes Signature

    ######################### 第三方开源库 #########################

    #okhttp
    -dontwarn com.squareup.okhttp.**
    -keep class com.squareup.okhttp.** { *;}
    -keep interface com.squareup.okhttp.** { *; }
    -keep class okio.**{*;}
    -dontwarn okio.**

    #butterknife
    -keep class butterknife.** { *; }
    -dontwarn butterknife.internal.**
    -keep class **$$ViewBinder { *; }

    -keepclasseswithmembernames class * {
        @butterknife.* <fields>;
    }

    -keepclasseswithmembernames class * {
        @butterknife.* <methods>;
    }

    #eventbus 3.0
    -keepclassmembers class ** {
        @org.greenrobot.eventbus.Subscribe <methods>;
    }
    -keep enum org.greenrobot.eventbus.ThreadMode { *; }
    # Only required if you use AsyncExecutor
    -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
        <init>(java.lang.Throwable);
    }

    #rxAndroid
    -dontwarn rx.internal.util.unsafe.**
    -keep class rx.android.** {*;}

    #Gson防混淆
    -keep class sun.misc.Unsafe { *; }
    -keep class com.idea.fifaalarmclock.entity.***
    -keep class com.google.gson.stream.** { *; }
    -keep class com.king.reading.data.**
    -keep class com.king.reading.data.** { *;}

    #rxjava
    -dontwarn io.reactivex.**
    -keep class io.reactivex.** {*;}

    #retrofit
    -dontwarn retrofit2.**
    -keep class retrofit2.** { *; }
    -keepclasseswithmembers class * {
        @retrofit.http.* <methods>;
    }

    #aRouter
    -dontwarn com.alibaba.android.arouter.**
   -keep public class com.alibaba.android.arouter.routes.**{*;}
   -keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

    #tars
    -dontwarn com.qq.tars.**
    -keep class com.qq.tars.** { *; }

    #glide
    -keep public class * implements com.bumptech.glide.module.GlideModule
    -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
    }

    #dbflow
    -keep class * extends com.raizlabs.android.dbflow.config.DatabaseHolder { *; }
    -keep class corg.json.alipay.** {*;}

    #Matisse图片浏览服务
    -dontwarn com.squareup.picasso.**

    #阿里云推送
    -keepclasseswithmembernames class ** {
        native <methods>;
    }
    -keepattributes Signature
    -keep class sun.misc.Unsafe { *; }
    -keep class com.taobao.** {*;}
    -keep class com.alibaba.** {*;}
    -keep class com.alipay.** {*;}
    -keep class com.ut.** {*;}
    -keep class com.ta.** {*;}
    -keep class anet.**{*;}
    -keep class anetwork.**{*;}
    -keep class org.android.spdy.**{*;}
    -keep class org.android.agoo.**{*;}
    -keep class android.os.**{*;}
    -dontwarn com.taobao.**
    -dontwarn com.alibaba.**
    -dontwarn com.alipay.**
    -dontwarn anet.**
    -dontwarn org.android.spdy.**
    -dontwarn org.android.agoo.**
    -dontwarn anetwork.**
    -dontwarn com.ut.**
    -dontwarn com.ta.**

    # 小米通道
    -keep class com.xiaomi.** {*;}
    -dontwarn com.xiaomi.**
    # 华为通道
    -keep class com.huawei.** {*;}
    -dontwarn com.huawei.**

    #阿里云oss
    -keep class com.alibaba.sdk.android.oss.** { *; }
    -dontwarn okio.**
    -dontwarn org.apache.commons.codec.binary.**

    ######################### 其他 #########################
    #junit
    -dontwarn org.junit.**
    -dontwarn android.test.**
    -dontwarn org.bouncycastle.**

    -dontwarn org.apache.**
    -keep class org.apache.**{*;}
    -keep class com.king.reading.**{*;}
