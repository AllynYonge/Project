package com.king.reading.module.learn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.common.collect.Lists;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.common.utils.DialogUtils;
import com.king.reading.data.entities.WordEntity;
import com.king.reading.ddb.Word;
import com.king.reading.common.utils.SpUtils;
import com.king.reading.widget.DonutProgress;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayerUtil;

import static com.king.reading.C.ROUTER_LEARN_WORDLISTEN_DETAIL;

/**
 * Created by AllynYonge on 22/06/2017.
 */

@Route(path = ROUTER_LEARN_WORDLISTEN_DETAIL)
public class WordListenDetailActivity extends BaseActivity {

    @BindView(R.id.tv_timer_tips)
    TextView mTvTimerTips;
    @BindView(R.id.tv_ready_word_tips)
    TextView mTvReadyWordTips;
    @BindView(R.id.progress_wheel)
    DonutProgress mProgressWheel;
    @BindView(R.id.iv_listen_word_play_stop)
    ImageView mIvListenWordPlayStop;
    @BindView(R.id.tv_listen_word_tips)
    TextView mTvListenWordTips;
    @BindView(R.id.iv_listen_word_order)
    ImageView mIvListenWordOrder;
    @BindView(R.id.tv_listen_word_order)
    TextView mTvListenWordOrder;
    @BindView(R.id.ll_listen_word_order)
    LinearLayout mLlListenWordOrder;
    @BindView(R.id.tv_speed_time)
    TextView mTvSpeedTime;
    @BindView(R.id.ll_listen_word_speed_timer)
    LinearLayout mLlListenWordSpeedTimer;

    @Autowired
    public long unitId;

    private List<WordEntity> mWords = Lists.newArrayList();
    private List<WordEntity> mRandomWords = Lists.newArrayList();
    private boolean mIsListenWordRandom;//单词听写顺序的标记
//    private String mSpeedTag;//速率播放的设置，1-0.8倍；2-1倍；3-1.3倍
    private int mListenWordTimer;//听写的间隔时间
    private int mListenWordIndex;
    private boolean mIsTimer = true;
    private int mCurrenTimeCount;
    private Timer mTimer;
    private int mListenWordCount;
    private boolean mIsTimerStop;
    private float mListenWordSpeed = 1.0f;
    private int mSpListenWordTimer;


    @Override
    public void onInitData(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        getResRepository().getWords(unitId).subscribe(new Consumer<List<WordEntity>>() {
            @Override
            public void accept(@NonNull List<WordEntity> wordEntities) throws Exception {
                mWords.clear();
                mWords.addAll(wordEntities);
                mIvListenWordPlayStop.setTag(0);//0-暂停状态；1-正在播放;2-暂停
                updateWordNumnber(-1);

                mIsListenWordRandom = SpUtils.sharePreGetBoolean(SysApplication.getApplication(), "isListenWordSequens");
                mIvListenWordOrder.setImageResource(mIsListenWordRandom == true ? R.mipmap.ic_listen_random : R.mipmap.ic_listen_sequens);
                mTvListenWordOrder.setText(mIsListenWordRandom == true ? "随机播放" : "顺序播放");
                setRandomWords();

        /*mSpeedTag = SpUtils.sharePreGet(SysApplication.getApplication(), "speedTag");
        if (TextUtils.isEmpty(mSpeedTag)) {
            mSpeedTag = "2";
        }*/

//        mListenWordSpeed = SpUtils.sharePreGetFloat(SysApplication.getApplication(),"listenWordSpeed");
                mListenWordSpeed = SpUtils.sharePreGetFloat(SysApplication.getApplication(),"listenWordSpeed");

                mListenWordTimer = SpUtils.sharePreGetInt(SysApplication.getApplication(), "listenWordTimer");
                if (mListenWordTimer == 0) {
                    mListenWordTimer = 5;
                }
                mTvTimerTips.setText(mListenWordTimer + "s");

                mProgressWheel.setMax(mListenWordTimer);
            }
        });
    }


    private void setRandomWords() {
        mRandomWords.clear();

        if (mIsListenWordRandom){
            int i;
            Random random = new Random();
            List<Integer> list = new ArrayList<>();
            while (list.size()<mWords.size()){
                i = random.nextInt(mWords.size());
                if (!list.contains(i)){
                    list.add(i);
                    mRandomWords.add(mWords.get(i));
                }
            }
        }else{
            for (WordEntity word : mWords) {
                mRandomWords.add(word);
            }
        }
    }

    @Override
    public void onInitView() {
        setCenterTitle("Unit1");
        setLeftImageIcon(R.mipmap.ic_back);
        setRightText("目录");
    }

    @OnClick(R.id.ll_listen_word_order)
    public void setListenWordOrder(View view){
        //暂停播放
        if ((Integer)mIvListenWordPlayStop.getTag()==1){
            stop();
        }
        String title = mIsListenWordRandom==true?"要按单词顺序听写吗？":"要打乱单词顺序，随机听写吗？";
        String confirm = mIsListenWordRandom==true?"顺序听写":"随机听写";
        DialogUtils.showConfirmDialog(this, "", title, "取消", confirm, new DialogUtils.IDialogButtonCallback() {
            @Override
            public void onPositiveBtnClick() {
                Toast.makeText(SysApplication.getApplication(),"确认",Toast.LENGTH_SHORT).show();
                mIsListenWordRandom = !mIsListenWordRandom;
                SpUtils.sharePreSaveBoolean(SysApplication.getApplication(),"isListenWordSequens",mIsListenWordRandom);
                initIsListenWordRandomView();
            }

            @Override
            public void onNegativeBtnClick() {
                Toast.makeText(SysApplication.getApplication(),"取消",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.ll_listen_word_speed_timer)
    public void setSpeedAndTimer(View view){
        //暂停播放
        if ((Integer)mIvListenWordPlayStop.getTag()==1){
            stop();
        }
        DialogUtils.listenWordSpeedRateAndTimerDialog(this,mHandler);
    }

    /**
     * 初始化随机播放或顺序播放的视图
     */
    private void initIsListenWordRandomView() {
        mCurrenTimeCount = mListenWordTimer;
        mTvTimerTips.setText(mCurrenTimeCount+"s");
        mProgressWheel.setProgress(mListenWordTimer);
        mIsTimer = true;
        mIsTimerStop = false;
        mListenWordIndex = 0;
        updateWordNumnber(-1);
        setRandomWords();
        mIsListenWordRandom = SpUtils.sharePreGetBoolean(SysApplication.getApplication(), "isListenWordSequens");
        mIvListenWordOrder.setImageResource(mIsListenWordRandom == true ? R.mipmap.ic_listen_random : R.mipmap.ic_listen_sequens);
        mTvListenWordOrder.setText(mIsListenWordRandom == true ? "随机播放" : "顺序播放");
    }

    @OnClick(R.id.ll_listen_word_speed_timer)
    public void setListenWordSpeedAndTimer(View view){

    }



    @Override
    public int getContentView() {
        return R.layout.activity_wordlistendetail;
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case C.LISTEN_WORD_TIMER:
                    if (mCurrenTimeCount <= mListenWordTimer) {
                        mTvTimerTips.setText(mListenWordTimer - mCurrenTimeCount + "s");
                        mProgressWheel.setProgress(mListenWordTimer-mCurrenTimeCount);
                        mCurrenTimeCount++;
                    } else {
                        mCurrenTimeCount = 0;
                        mIsTimer = false;
                        mTimer.cancel();
                        goToListenWord();
                    }
                    break;
                case C.LISTEN_WORD_SPEED:
                    mListenWordSpeed = SpUtils.sharePreGetFloat(SysApplication.getApplication(),"listenWordSpeed");
                    mSpListenWordTimer = SpUtils.sharePreGetInt(SysApplication.getApplication(), "listenWordTimer");
                    if (mSpListenWordTimer<= mListenWordTimer){//时间间隔设置小了，直接按最小的间隔时间来听写
                        mCurrenTimeCount =0;
                        mListenWordTimer = mSpListenWordTimer;
                    }else{//时间间隔设置大了，当前单词保持原来的倒计时，下一个单词按设置的来进行倒计时
                        int i = mSpListenWordTimer - mListenWordTimer;
                        mListenWordTimer = mSpListenWordTimer;
                        mCurrenTimeCount +=i;
                    }

                    break;
            }
        }
    };


    @OnClick(R.id.iv_listen_word_play_stop)
    public void playOrStop(View view) {
        mTvReadyWordTips.setVisibility(View.INVISIBLE);

        if ((Integer) mIvListenWordPlayStop.getTag() == 0) {

            updateWordNumnber(mListenWordIndex);
            mIvListenWordPlayStop.setTag(1);
            mIvListenWordPlayStop.setImageResource(R.mipmap.ic_listen_stop);
            if (mIsTimer) {//朗读单词前的倒计时
                if (!mIsTimerStop) {
                    mIsTimerStop = true;
                    sendTimer();
                } else {
                    mIsTimerStop = false;
                    mIvListenWordPlayStop.setImageResource(R.mipmap.ic_listen_play);
                    mTimer.cancel();
                }
            } else {//直接朗读
                goToListenWord();
            }
        } else if ((Integer) mIvListenWordPlayStop.getTag() == 1) {//暂停
            stop();
        }

    }

    private void stop() {
        mIvListenWordPlayStop.setTag(0);
        mIvListenWordPlayStop.setImageResource(R.mipmap.ic_listen_play);
        if (mIsTimer) {//进入倒计时的暂停
            if (mIsTimerStop) {
                mIsTimerStop = false;
                mTimer.cancel();
            } else {
                mIsTimerStop = true;
                sendTimer();
            }
        } else {
            MediaPlayerUtil.getInstance().setOnCompletionListener(null);
            MediaPlayerUtil.getInstance().stop();
        }
    }

    private void goToListenWord() {

        MediaPlayerUtil.playFromIntenet(SysApplication.getApplication(), mRandomWords.get(mListenWordIndex).encryptSoundURL, mListenWordSpeed, null);
        MediaPlayerUtil.getInstance().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                if (mListenWordCount < 1) {//第一遍
                    mListenWordCount++;
                    goToListenWord();
                } else {

                    if (mListenWordIndex < mWords.size() - 1){
                        mListenWordCount = 0;
                        mListenWordIndex++;
                        updateWordNumnber(mListenWordIndex);
                        mIsTimer = true;
                        sendTimer();
                    }else{//朗读到最后一个单词
                        EventBus.getDefault().postSticky(mRandomWords);
                        startActivity(new Intent(WordListenDetailActivity.this,ListenCompleteActivity.class));
                    }
                }
            }
        });

    }

    private void sendTimer() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(C.LISTEN_WORD_TIMER);
            }
        }, 0, 1000);
    }

    /**
     * // 使用富文本改变显示播放第几个单词的提示的颜色
     *
     * @param index
     */
    protected void updateWordNumnber(int index) {

        int size = mWords.size();
        SpannableString sp = new SpannableString("正在播放第" + (index + 1) + "个单词，共" + size + "个单词");
        if (size < 10) {

            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ffb400")), 5, 6, 1);
            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ffb400")), 11, 12, 2);
            // 第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素，同上。
            sp.setSpan(new AbsoluteSizeSpan(30, true), 5, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new AbsoluteSizeSpan(30, true), 11, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (index >= 9) {

            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ffb400")), 5, 7, 1);
            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ffb400")), 12, 14, 2);
            // 第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素，同上。
            sp.setSpan(new AbsoluteSizeSpan(30, true), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new AbsoluteSizeSpan(30, true), 12, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {

            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ffb400")), 5, 6, 1);
            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ffb400")), 11, 13, 2);
            // 第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素，同上。
            sp.setSpan(new AbsoluteSizeSpan(30, true), 5, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new AbsoluteSizeSpan(30, true), 11, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mTvListenWordTips.setText(sp);
    }


}
