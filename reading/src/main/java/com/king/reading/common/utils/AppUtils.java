package com.king.reading.common.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.model.WordResult;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class AppUtils {

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序版本名称信息
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            Logger.e(e.toString());
        }
        return null;
    }

    /**
     * 获取应用程序版本名称信息
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;

        } catch (NameNotFoundException e) {
            Logger.e(e.toString(), e);
        }
        return 0;
    }

    /**
     * whether application is in background
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * whether this process is named with processName
     * 检查当前运行的进程是否与你传入的进程名一致
     */
    public static boolean isNamedProcess(Context context, String processName) {
        if (context == null) {
            return false;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
        if (processInfoList == null) {
            return true;
        }

        for (RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid && isEquals(processName, processInfo.processName)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEquals(Object actual, Object expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }

    /**
     * 获取手机的总内存大小 单位byte
     */
    public static long getTotalMem() {
        try {
            //在/proc/meminfo这个文件下是存储内存相关信息
            FileInputStream fis = new FileInputStream(new File("/proc/meminfo"));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String totalInfo = br.readLine();
            //MemTotal:         513000 kB
            StringBuffer sb = new StringBuffer();
            for (char c : totalInfo.toCharArray()) {
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                }
            }
            return Long.parseLong(sb.toString()) * 1024;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取可用的内存信息。
     */
    public static long getAvailMem(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取内存大小
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.availMem;
    }

    /**
     * 得到正在运行的进程的数量
     */
    public static int getRunningPocessCount(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();
        return runningAppProcessInfos.size();
    }

    /**
     * 判断某个app是否在手机中安装
     * @param packageName
     * @param context
     * @return
     */
    public static boolean isPackageInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static void exitApp(){
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public static Application getApplicationUsingReflection(){
        try {
            return (Application) Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication").invoke(null, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置头像
     * @param v
     */
    public static void setHeadImage(final ImageView v, Activity activity) {
        RxPermissions rxPermissions = new RxPermissions(activity);


        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            String url = "http://192.168.3.197:8080/user.png";
                            boolean headImageExits = FileUtils.getFileIsExits(Environment.getExternalStorageDirectory().getAbsolutePath(), "HeadImage.png");
                            if (headImageExits) {
                                Bitmap bitmapFromFile = BitmapUtils.getBitmapFromFile(new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "HeadImage.png"), 0, 0);
                                Drawable headImageDrawable = new BitmapDrawable(bitmapFromFile);
                                Glide.with(SysApplication.getApplication()).load(url).error(headImageDrawable).into(v);
                            }else {
                                Glide.with(SysApplication.getApplication()).load(url).error(R.drawable.temp_logo).into(v);
                            }

                        } else {
                            Toast.makeText(SysApplication.getApplication(), "权限阻止", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * @param @param textView
     * @param @param wordResults 设定文件
     * @return void 返回类型
     * @throws
     * @Title: setViewTextColor
     * @Description: TODO(设置跟读闯关评测结果单词对应颜色显示)
     */
    public static void setViewTextColor(TextView textView, List<WordResult> wordResults) {

        String replaceMent;

        Log.d("wordResults", "wordResults=" + wordResults);
        String text = textView.getText().toString().trim();
        SpannableString sp = new SpannableString(textView.getText().toString().trim());
        for (int i = 0; i < wordResults.size(); i++) {
            if (!wordResults.get(i).getText().equals("sil")) {
                String subSequence;
                String text1 = wordResults.get(i).getText();
                // 不等于分隔符
                int start = text.indexOf(wordResults.get(i).getText());
                if (start == -1)
                    continue;
                int end = start + wordResults.get(i).getText().length();
                // 下面的这种情况防止有些句子后面没有标点符号，出现字符串下标越界异常的情况
                if (end < text.length()) {
                    subSequence = text.substring(end, end + 1);
                } else {
                    subSequence = " ";
                }
                if (subSequence.equals(" ")) {// 没有标点符号

                    replaceMent = createLengthString(end - start);
                    text = text.replaceFirst(wordResults.get(i).getText(), replaceMent);
                } else {// 有标点符号
                    end++;// 让标点符号显示颜色
                    replaceMent = createLengthString(end - start + 1);
                    text = text.replaceFirst(wordResults.get(i).getText() + 1, replaceMent);
                }
                // replaceMent = createLengthString(end - start);

                // text = text.replaceFirst(wordResults.get(i).getText(),
                // replaceMent);
                if (wordResults.get(i).getScore() < 6d) {
                    sp.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    Log.d("wordResults", "word=" + wordResults.get(i).getText() + ",score=" + wordResults.get(i).getScore());
                } else if (wordResults.get(i).getScore() < 8d) {
                    sp.setSpan(new ForegroundColorSpan(textView.getContext().getResources().getColor(R.color.text_title_black)), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    Log.d("wordResults", "word=" + wordResults.get(i).getText() + ",score=" + wordResults.get(i).getScore());
                } else {
                    sp.setSpan(new ForegroundColorSpan(textView.getContext().getResources().getColor(R.color.text_title_green)), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    Log.d("wordResults", "word=" + wordResults.get(i).getText() + ",score=" + wordResults.get(i).getScore());
                }
            }
        }
        textView.setText(sp);
    }

    public static String createLengthString(int length) {
        String str = "";
        for (int i = 0; i < length; i++) {
            str += "#";
        }
        return str;
    }
}
