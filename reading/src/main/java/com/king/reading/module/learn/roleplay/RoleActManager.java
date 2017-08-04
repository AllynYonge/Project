package com.king.reading.module.learn.roleplay;

import android.content.Context;
import android.util.Log;

import com.king.reading.common.utils.VoiceEvaluate;
import com.king.reading.model.EvaluateResult;
import com.king.reading.model.RoleElement;
import com.unisound.edu.oraleval.sdk.sep15.IOralEvalSDK;
import com.unisound.edu.oraleval.sdk.sep15.SDKError;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 创建者     王开冰
 * 创建时间   2017/7/27 14:35
 * 描述	      ${TODO}角色扮演录音的管理类
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class RoleActManager implements IOralEvalSDK.ICallback {

    private final static String TAG = "RoleActManager";
    // 默认状态
    public static final int STATE_NONE = 0;
    // 进行中
    public static final int STATE_LOADING = 1;

    private VoiceEvaluate mEvaluate;
    private static RoleActManager instance;
    private List<RoleActObserver> observers = new ArrayList<RoleActObserver>();// 保存注册的观察者对象
    private Timer timer;
    private TimerTask task;
    private RoleElement element;
    private int mDuration;
    private boolean stop = false;
    private boolean cancelEvaluate = false;


    private RoleActManager(Context context) {
        mEvaluate = new VoiceEvaluate(context);
        mEvaluate.setCallback(this);
    }

    public static synchronized RoleActManager getInstance(Context context) {
        if (instance == null) {
            instance = new RoleActManager(context);
        }
        return instance;
    }


    /**
     * @param @param observer 设定文件
     * @return void 返回类型
     * @throws
     * @Title: registerObserver
     * @Description: TODO(注册观察者)
     */
    public void registerObserver(RoleActObserver observer) {
        synchronized (observer) {
            if (!observers.contains(observer)) {
                observers.add(observer);
            }
        }
    }

    /**
     * @param @param observer 设定文件
     * @return void 返回类型
     * @throws
     * @Title: unRegisterObserver
     * @Description: TODO(撤销观察者)
     */
    public void unRegisterObserver(RoleActObserver observer) {
        synchronized (observer) {
            if (observers.contains(observer)) {
                observers.remove(observer);
            }
        }
    }

    /**
     * @param @param follow 设定文件
     * @return void 返回类型
     * @throws
     * @Title: notifyDownloadStateChanged
     * @Description: TODO(通知所有观察者状态发生了变化)
     */
    public void notifyStateChanged(RoleElement element) {
        synchronized (observers) {
            for (RoleActObserver observer : observers) {
                observer.onStateChanged(element);
            }
        }
    }

    /**
     * @param @param follow 设定文件
     * @return void 返回类型
     * @throws
     * @Title: notifyProgressed
     * @Description: TODO(通知所有观察者进度发生了变化)
     */
    public void notifyProgressed(RoleElement element) {
        synchronized (observers) {
            for (RoleActObserver observer : observers) {
                observer.onProgressed(element);
            }
        }
    }

    public void notifyShowStopDialog() {
        synchronized (observers) {
            for (RoleActObserver observer : observers) {
                observer.showStopDialog();
            }
        }
    }

    public void notifystartRoleAct() {
        synchronized (observers) {
            for (RoleActObserver observer : observers) {
                observer.startRoleAct();
            }
        }
    }

    public void notifyShowErrorMsg(SDKError err) {
        synchronized (observers) {
            for (RoleActObserver observer : observers) {
                observer.showErrorMsg(err);
            }
        }
    }

    public void notifySetStopButtonEnabled(boolean enabled) {
        synchronized (observers) {
            for (RoleActObserver observer : observers) {
                observer.setStopButtonEnable(enabled);
            }
        }
    }

    public VoiceEvaluate getEvaluate() {
        return mEvaluate;
    }

    public synchronized void acting(final RoleElement element) {
        cancelTimer();
        this.element = element;

        mDuration = 5;
        element.setState(STATE_LOADING);// 开始录音
        element.setCurProgress(0);
        element.setMaxProgress(mDuration);
        notifyStateChanged(element);// 通知观察者
        mEvaluate.evaluate(element.getText());// 设置评测内容
        Log.e(TAG, "扮演开始");
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (element.getCurProgress() < element.getMaxProgress()
                        && element.getState() == STATE_LOADING) {
                    // 进行中
                    element.setCurProgress(element.getCurProgress() + 1);
                    notifyProgressed(element);
                }
                if (element.getCurProgress() >= element.getMaxProgress()) {
                    // 停止
                    Log.e(TAG, "停止");
                    notifySetStopButtonEnabled(false);
                    element.setState(STATE_NONE);
                    mEvaluate.stopIOralEvalSDK();
                    notifyStateChanged(element);
                    cancelTimer();
                }
            }
        };
        timer.schedule(task, 0, 1000);

    }

    public synchronized void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public synchronized void stop(boolean cancelEvaluate) {
        if (element != null) {
            stop = true;
            this.cancelEvaluate = cancelEvaluate;
            mEvaluate.stopIOralEvalSDK();
            element.setState(STATE_NONE);
            element.setCurProgress(element.getMaxProgress());
            notifyStateChanged(element);
        }
    }

    public interface RoleActObserver {

        void onStateChanged(RoleElement element);// 当声音播放状态变化时

        void onProgressed(RoleElement element);// 正在进行中

        void showStopDialog();//弹出停止对话框

        void startRoleAct();//扮演

        void showErrorMsg(SDKError err);//提示错误信息

        void setStopButtonEnable(boolean enabled);//设置停止按钮是否可点击
    }

    @Override
    public void onStart(IOralEvalSDK iOralEvalSDK, int i) {
        Log.i(TAG, "onStart:" + i);
    }

    @Override
    public void onError(IOralEvalSDK iOralEvalSDK, SDKError sdkError, IOralEvalSDK.OfflineSDKError offlineSDKError) {
        Log.e(TAG, "onError:" + element.getText());
        final SDKError err = sdkError;
        final IOralEvalSDK.OfflineSDKError ofe = offlineSDKError;
        mEvaluate.stopIOralEvalSDK();
        if (err != null) {
            Log.e(TAG, "onError--->err: " + err.toString());
            notifyShowErrorMsg(err);
        }
        cancelTimer();
        element.setState(STATE_NONE);
        element.setCurProgress(element.getMaxProgress());
        notifyStateChanged(element);
        if (!cancelEvaluate)
            notifyShowStopDialog();//弹出停止对话框
        if (stop) {
            Log.e(TAG, "onError完下一步操作");
            notifystartRoleAct();//进入下一步操作
        }
        stop = false;
        cancelEvaluate = false;
        notifySetStopButtonEnabled(true);
    }

    @Override
    public void onStop(IOralEvalSDK iOralEvalSDK, String s, boolean b, String s1, IOralEvalSDK.EndReason endReason) {
        Log.e(TAG, "onStop:" + s);
        if (s != null && !cancelEvaluate) {
            EvaluateResult result = mEvaluate
                    .parseJsonResult(s);
            final int score = (int) result.getLines().get(0).getScore();
            element.setScore(score);
            if (!stop) {
                Log.e(TAG, "onStop完下一步操作");
                notifystartRoleAct();//进入下一步操作
            }
        } else {
            cancelEvaluate = false;
            notifyShowStopDialog();//弹出停止对话框
        }
        stop = false;
        notifySetStopButtonEnabled(true);
    }

    @Override
    public void onVolume(IOralEvalSDK iOralEvalSDK, int i) {
        Log.i(TAG, "Volume:" + i);
    }

    @Override
    public void onAudioData(IOralEvalSDK iOralEvalSDK, byte[] bytes, int i, int i1) {
        Log.i(TAG, "got " + i1 + " bytes of pcm 录音中......offset:" + i + "------len：" + i1);
    }
}
