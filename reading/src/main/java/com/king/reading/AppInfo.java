package com.king.reading;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.king.reading.common.utils.AppUtils;
import com.king.reading.common.utils.NetworkUtils;
import com.king.reading.common.utils.PermissionsChecker;
import com.orhanobut.logger.Logger;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;


public class AppInfo {

    // device info
    public static String imei;              // imei
    public static String mac;               // mac地址
    public static String sdkName;           // android 系统版本名，eg：4.4.1
    public static int sdkVersion;           // android 系统版本号，整形 eg:17
    public static String brand;             // 手机品牌，eg：三星（SAMSUNG）
    public static String model;             // 手机型号，eg：S4, iPhone,3,1, iPhone,3,2
    public static int deviceType;    // iOS,Android

    // client info
    public static String versionName;   // 客户端版本名，eg：2.x.x,<YXY>_[1-9]_[0-9]_[0-9]
    public static int versionCode;      // 客户端versionCode, versionName->int
    public static int buildNum;         // build号 xxxx
    public static String channel;       // 渠道号 TBT,91SZ,APP,

    // net info
    // 客户端网络类型，0=UN_DETECT, 1=WIFI, 2=CMWAP, 3=CMNET, 4=UNIWAP, 5=UNINET, 6=WAP3G,
    // 7=NET3G, 8=CTWAP, 9=CTNET, 10=UNKNOWN, 11=UNKNOW_WAP, 12=NO_NETWORK
    public static byte netType;
    public static byte ipType;   // ip接入方式，0=预埋，1=就近接入，2=域名
    public static int requestIp; // 对ip接入方式，填写IP的网络字节序整数

    private AppInfo() {
    }

    private static AppInfo mInstance = null;

    public static AppInfo getInstance() {
        if (mInstance == null) {
            synchronized (AppInfo.class) {
                if (mInstance == null) {
                    mInstance = new AppInfo();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionsChecker.lacksPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            imei = "";
        } else {
            imei = getIMEI(context);
        }
        mac = getMAC(context);
        sdkName = getSDKName();
        sdkVersion = getSDKVersion();
        brand = getBrand();
        model = getModel();
        //4表示是Android平台
        deviceType = 4;

        versionName = AppUtils.getVersionName(context);
        versionCode = AppUtils.getVersionCode(context);
        buildNum = getBuildNum(context);
        channel = getChannel(context);

        netType = NetworkUtils.getConnectedType(context).value;
        ipType = getIPType();
        requestIp = getRequestIP();

        Logger.d("init global config:" + "\n" +
                "imei:" + imei + "\n" +
                "mac:" + mac + "\n" +
                "sdkName:" + sdkName + "\n" +
                "sdkVersion:" + sdkVersion + "\n" +
                "brand:" + brand + "\n" +
                "model:" + model + "\n" +
                "deviceType:" + deviceType + "\n" +
                "versionName:" + versionName + "\n" +
                "versionCode:" + versionCode + "\n" +
                "buildNum:" + buildNum + "\n" +
                "channel:" + channel + "\n" +
                "netType:" + netType + "\n" +
                "ipType:" + ipType + "\n" +
                "requestIp:" + requestIp
        );
    }

    public void setImei(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionsChecker.lacksPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            imei = "";
        } else {
            imei = getIMEI(context);
        }
    }

    public String getQUA() {
        return "YXY_Android_" + versionName + "_" + channel;
    }

    public String getVersionCode() {
        return String.valueOf(versionCode);
    }

    private String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    private String getMAC(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    private String getSDKName() {
        return Build.VERSION.RELEASE;
    }

    private int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    private String getBrand() {
        return Build.BRAND;
    }

    private String getModel() {
        return Build.MODEL;
    }

    private static final String BUILD_NUM = "BUILD_NUM";

    private int getBuildNum(Context context) {
        int buildNum = 0;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            //manifest里面的name值
            buildNum = ai.metaData.getInt(BUILD_NUM);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buildNum;
    }

    private static final String CHANNEL = "KING_CHANNEL";

    private String getChannel(Context context) {
        String channelid = "";
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            //manifest里面的name值
            Object value = ai.metaData.get(CHANNEL);
            if (value != null) {
                channelid = value.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channelid;
    }

    private byte getIPType() {
        return 2;
    }

    private int getRequestIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        String ip = inetAddress.getHostAddress();
                        return ipStringToInt(ip);
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    // 把String类型的Ip地址转换成int类型
    private static int ipStringToInt(String hostname) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(hostname);
            byte[] addrBytes = inetAddress.getAddress();
            return (addrBytes[3] & 0xff) << 24 | ((addrBytes[2] & 0xff) << 16) | ((addrBytes[1] & 0xff) << 8) | (addrBytes[0] & 0xff);
        } catch (UnknownHostException ignored) {
            ignored.printStackTrace();
        }
        return -1;
    }
}
