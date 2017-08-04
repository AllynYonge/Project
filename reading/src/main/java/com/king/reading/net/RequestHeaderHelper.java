package com.king.reading.net;

import com.blankj.utilcode.util.EmptyUtils;
import com.king.reading.AppInfo;
import com.king.reading.C;
import com.king.reading.data.entities.UserEntity;
import com.king.reading.data.entities.UserEntity_Table;
import com.king.reading.mod.Device;
import com.king.reading.mod.Header;
import com.king.reading.mod.Platform;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by AllynYonge on 02/08/2017.
 */

public class RequestHeaderHelper {
    private static RequestHeaderHelper mInstance;
    private static final Header header = new Header();
    private static AtomicInteger mRequestId = new AtomicInteger(1);

    public static RequestHeaderHelper getInstance() {
        if (mInstance == null) {
            synchronized (RequestHeaderHelper.class) {
                if (mInstance == null) {
                    mInstance = new RequestHeaderHelper();
                    header.setPlatform(Platform.Android.value());
                    header.setAppId(C.KINGSUN_APP_ID);
                    header.setDevice(new Device(AppInfo.sdkName, AppInfo.sdkVersion,
                            AppInfo.brand, AppInfo.model, AppInfo.versionName,
                            AppInfo.versionCode, AppInfo.buildNum, AppInfo.channel));
                }
            }
        }
        return mInstance;
    }

    public void replaceHeader(Header header){
        UserEntity loginUser = getUserEntity();
        if (EmptyUtils.isNotEmpty(loginUser)){
            loginUser.account = header.account;
            loginUser.userId = header.userId;
            loginUser.token = header.getToken();
            loginUser.refreshToken = header.getRefreshToken();
            loginUser.save();
        }
        header.setAccount(header.getAccount());
        header.setUserId(header.getUserId());
        header.setToken(header.getToken());
        header.setRefreshToken(header.getRefreshToken());
    }

    public Header createHeader(String requestName){
        //todo 登陆的token必须为空
        UserEntity loginUser = getUserEntity();
        if (EmptyUtils.isNotEmpty(loginUser)){
            header.setToken(loginUser.token);
            header.setAccount(loginUser.account);
            header.setRefreshToken(loginUser.refreshToken);
            header.setUserId(loginUser.userId);
        }
        header.setRequestName(requestName);
        header.setRequestId(mRequestId.incrementAndGet());
        return header;
    }

    private UserEntity getUserEntity(){
        return SQLite.select().from(UserEntity.class).where(UserEntity_Table.token.notEq("")).querySingle();
    }
}
