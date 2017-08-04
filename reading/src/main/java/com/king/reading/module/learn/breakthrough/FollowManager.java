package com.king.reading.module.learn.breakthrough;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.king.reading.C;
import com.king.reading.SysApplication;
import com.king.reading.common.utils.FileUtils;
import com.king.reading.common.utils.VoiceEvaluate;
import com.king.reading.model.EvaluateResult;
import com.king.reading.model.Follow;
import com.king.reading.model.WordResult;
import com.unisound.edu.oraleval.sdk.sep15.IOralEvalSDK;
import com.unisound.edu.oraleval.sdk.sep15.SDKError;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayerUtil;


/**
 * 创建者     王开冰
 * 创建时间   2017/7/29 14:30
 * 描述	      跟读闯关录音的管理类
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class FollowManager implements IOralEvalSDK.ICallback {

    private final static String TAG = "FollowManager";
    // 默认状态
    public static final int STATE_NONE = 0;
    // 进行中
    public static final int STATE_LOADING = 1;
    // 暂停
    public static final int STATE_PAUSE = 2;

    private static FollowManager instance;

    private List<FollowObserver> observers = new ArrayList<FollowObserver>();// 保存注册的观察者对象
    private Follow follow;

    private Context context;

    private Timer timer;
    private TimerTask task;
    private VoiceEvaluate evaluate;

    private String recordPath = "";
    private String recordName = "";
    private FileOutputStream audioFileOut;
    private boolean auto = false;
    private boolean evaluateing = false;
    private Handler mHandler;
    private boolean cancelEvaluate = false;
    private int mDuration;

    private FollowManager(Context context) {
        this.context = context;
        evaluate = new VoiceEvaluate(context);
        evaluate.setCallback(this);
        createRecordFolder();
    }

    public static synchronized FollowManager getInstance(Context context) {
        if (instance == null) {
            instance = new FollowManager(context);
        }
        return instance;
    }

    private void createRecordFolder() {
        FileUtils.createFileDirectory(C.folder_Res /*+ SpUtils.sharePreGet(context, "currCoursePath")*/ + C.FOLDER_RECORD);
        recordPath = C.folder_Res /*+ SpUtils.sharePreGet(context, "currCoursePath")*/ + C.FOLDER_RECORD;
    }

    public boolean isEvaluateing() {
        return evaluateing;
    }

    public void setEvaluateing(boolean evaluateing) {
        this.evaluateing = evaluateing;
    }

    public void setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    /**
     * @param @param observer 设定文件
     * @return void 返回类型
     * @throws
     * @Title: registerObserver
     * @Description: TODO(注册观察者)
     */
    public void registerObserver(FollowObserver observer) {
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
    public void unRegisterObserver(FollowObserver observer) {
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
    public void notifyStateChanged(Follow follow) {
        synchronized (observers) {
            for (FollowObserver observer : observers) {
                observer.onStateChanged(follow);
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
    public void notifyProgressed(Follow follow) {
        synchronized (observers) {
            for (FollowObserver observer : observers) {
                observer.onProgressed(follow);
            }
        }
    }

    public synchronized void recording(final Follow follow) {
        if (isEvaluateing())
            return;
        cancelTimer();
        stop(false);
        this.follow = follow;
        String soundPath = C.folder_Res + follow.getSound();


        mDuration = 5;

        follow.setrState(STATE_LOADING);// 开始录音
        follow.setrCurProgress(0);
        follow.setrMaxProgress(mDuration);
        notifyStateChanged(follow);// 通知观察者
        setEvaluateing(true);
        evaluate.evaluate(follow.getText());// 设置评测内容
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (follow.getrCurProgress() < follow.getrMaxProgress() && follow.getrState() == STATE_LOADING) {
                    // 进行中
                    follow.setrCurProgress(follow.getrCurProgress() + 1);
                    notifyProgressed(follow);
                }
                if (follow.getrCurProgress() >= follow.getrMaxProgress()) {
                    // 停止
                    follow.setrState(STATE_NONE);
                    if (evaluateing)
                        mHandler.sendEmptyMessage(C.FOLLOW_SHOW_EVALUATE_TIPS);
                    evaluate.stopIOralEvalSDK();
                    notifyStateChanged(follow);
                    cancelTimer();
                }
            }
        };
        timer.schedule(task, 0, 1000);
    }

    /**
     * 播放录音文件
     *
     * @param follow
     */
    public synchronized void playBack(final Follow follow) {
        cancelTimer();
        stop(true);
        this.follow = follow;
        String soundPath = follow.getRecordPath();
        MediaPlayerUtil.playFromSdCard(SysApplication.getApplication(), soundPath);
        MediaPlayerUtil.getInstance().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }
        });
    }

    public synchronized void stop(boolean cancelEvaluate) {
        if (follow != null) {
            cancelTimer();
            this.cancelEvaluate = cancelEvaluate;
            MediaPlayerUtil.stop();
            evaluate.stopIOralEvalSDK();
            follow.setrState(STATE_NONE);
            follow.setrCurProgress(follow.getrMaxProgress());
            notifyStateChanged(follow);
        }
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

    public interface FollowObserver {
        void onStateChanged(Follow follow);// 当声音播放状态变化时

        void onProgressed(Follow follow);// 正在进行中
    }

    @Override
    public void onStart(IOralEvalSDK iOralEvalSDK, int i) {
        Log.i(TAG, "onStart:" + i);
    }

    @Override
    public void onError(IOralEvalSDK iOralEvalSDK, SDKError sdkError, IOralEvalSDK.OfflineSDKError offlineSDKError) {
        closeAudioFileOut();
        final SDKError err = sdkError;
        final IOralEvalSDK.OfflineSDKError ofe = offlineSDKError;
        if (err != null) {
            Log.e(TAG, "onError--->err: " + err.toString());
            follow.setrState(STATE_NONE);
            follow.setrCurProgress(follow.getrMaxProgress());
            notifyStateChanged(follow);
            Message msg = new Message();
            msg.what = C.FOLLOW_EVALUATE_ERROR;
            msg.obj = err;
            if (evaluateing)
                mHandler.sendMessage(msg);
        }

        setEvaluateing(false);
    }

    @Override
    public void onStop(IOralEvalSDK iOralEvalSDK, String s, boolean b, String url, IOralEvalSDK.EndReason endReason) {
        closeAudioFileOut();
        if (s != null && !cancelEvaluate) {
            EvaluateResult result = evaluate.parseJsonResult(s);
            double score = 0;
            List<WordResult> words = new ArrayList<WordResult>();
            if (result.getLines() != null) {
                for (int i = 0; i < result.getLines().size(); i++) {
                    score += result.getLines().get(i).getScore();
                    words.addAll(result.getLines().get(i).getWords());
                }
                score = score / result.getLines().size();
                for (int i = 0; i < words.size(); i++) {
                    words.get(i).setParentID(follow.getParentID());
                    words.get(i).setUserName(follow.getUserName());
                    words.get(i).setParentText(follow.getText());
                    words.get(i).setSort(i);
                }
            }
            follow.setScore((float) score);
            follow.setWords(words);
            follow.setRecordUrl(url);
            follow.setRecordPath(recordPath + recordName);
            /*follow.setRecordCount(follow.getRecordCount() + 1);
            followProcess.update(follow);*/
            notifyStateChanged(follow);
            if (isEvaluateing()) {
                Message message = new Message();
                message.obj = follow;
                message.what = C.FOLLOW_CANCLE_EVALUATE_TIPS;
                mHandler.sendMessage(message);
            }
        } else {
            cancelEvaluate = false;
        }
        setEvaluateing(false);
    }

    @Override
    public void onVolume(IOralEvalSDK iOralEvalSDK, int i) {
        Log.i(TAG, "Volume:" + i);
    }

    @Override
    public void onAudioData(IOralEvalSDK iOralEvalSDK, byte[] bytes, int i, int i1) {
        Log.i(TAG, "got " + i1 + " bytes of pcm 录音中......offset:" + i + "------len：" + i1);
        try {
            File sound = new File(C.folder_Res + follow.getSound());
            recordName = sound.getName().substring(sound.getName().length() - 11, sound.getName().length());

            if (audioFileOut == null) {
                audioFileOut = new FileOutputStream(new File(new File(recordPath), recordName));
            }
            audioFileOut.write(bytes, i, i1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeAudioFileOut() {
        if (audioFileOut != null) {
            try {
                audioFileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            audioFileOut = null;
        }
    }
}
