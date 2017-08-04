package com.king.reading.common.oss;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.king.reading.SysApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by AllynYonge on 25/07/2017.
 */

public class AliOSSConfig {

    private static final String ACCESSKEYID = "LTAINCOefJhuyobi";
    private static final String ACCESSKEYSECRET = "cjusKlRsq08WSnNTS7mhY3uKXjPvIk";
    private static final String ENDPOINT = "vpc100-oss-cn-shenzhen.aliyuncs.com";

    public static final String OOSBUCKET_IMG = "talkwebsz-img";
    public static final String OOSBUCKET_AVATAR = "talkwebsz-avatar";
    public static final String OOSPATCH = "talkwebsz-android-patch";
    public static final String OOSBUCKET_RES = "talkwebsz-resource";

    public static final String RECORD_PROFIX = "record/";

    private OSSClient mOSSClient;

    private AliOSSConfig() {
    }

    private static AliOSSConfig mInstance = null;

    public static AliOSSConfig getInstance() {
        if (mInstance == null) {
            synchronized (AliOSSConfig.class) {
                if (mInstance == null) {
                    mInstance = new AliOSSConfig();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化阿里的配置
     */
    public void init() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(ACCESSKEYID, ACCESSKEYSECRET);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        mOSSClient = new OSSClient(SysApplication.getApplication(), ENDPOINT, credentialProvider, conf);
    }

    /**
     * 获取OSSClient
     */
    public OSSClient getOSSClient() {
        return mOSSClient;
    }

    /**
     * 获取成功上传阿里云服务器后的网址
     *
     * @param targetBucket bucket名字是什么
     * @param ObjectName   上传的文件名是什么
     * @return 成功后在阿里云的网址
     */
    public String getResourceURL(String targetBucket, String ObjectName) {
        try {
            ObjectName = URLEncoder.encode(ObjectName, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            if (OSSLog.isEnableLog()) {
                var3.printStackTrace();
            }
        }
        return "http://" + targetBucket + "." + AliOSSConfig.ENDPOINT + "/" + ObjectName;
    }
}
