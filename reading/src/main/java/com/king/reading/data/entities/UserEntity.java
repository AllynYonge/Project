package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by AllynYonge on 27/06/2017.
 */

@Table(database = AppDatabase.class, allFields = true, cachingEnabled = true)
public class UserEntity extends BaseModel{
    @PrimaryKey
    public long userId;
    public String token;
    public String account;
    public String refreshToken;
    public String nickName;
    public String avatar;
    public String schoolName;
    public String className;
    public long usingBook; //用户当前正在使用的书
    public boolean firstLogin;
    public boolean vip;
    public String courseIds;
    public String vipStartTime;
    public String vipEndTIme;
    public String vipDescription;

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", token='" + token + '\'' +
                ", account='" + account + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", className='" + className + '\'' +
                ", usingBook=" + usingBook +
                ", firstLogin=" + firstLogin +
                ", vip=" + vip +
                ", courseIds='" + courseIds + '\'' +
                ", vipStartTime='" + vipStartTime + '\'' +
                ", vipEndTIme='" + vipEndTIme + '\'' +
                ", vipDescription='" + vipDescription + '\'' +
                '}';
    }
}
