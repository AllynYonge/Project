package com.king.reading.module.learn.breakthrough;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.adapter.BreakThroughDetailPagerAdapter;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.common.utils.ACacheMgr;
import com.king.reading.common.utils.VoiceEvaluate;
import com.king.reading.ddb.Mission;
import com.king.reading.ddb.MissionItem;
import com.king.reading.model.BreakThroughList;
import com.king.reading.model.EventMessageBreakThroughResult;
import com.king.reading.model.Follow;
import com.king.reading.widget.BreakThroughViewPager;
import com.king.reading.widget.DonutProgress;
import com.king.reading.widget.SimpleRatingBar;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.unisound.edu.oraleval.sdk.sep15.SDKError;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayerUtil;

/**
 * Created by AllynYonge on 22/06/2017.
 */

public class BreakThroughDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener, FollowManager.FollowObserver {
    @BindView(R.id.pager)
    BreakThroughViewPager pager;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_progress)
    TextView mTvProgress;
    @BindView(R.id.imageView_forward)
    ImageView mImageViewForward;
    @BindView(R.id.imageView_back)
    ImageView mImageViewBack;
    @BindView(R.id.imageButton_try)
    ImageButton mImageButtonTry;
    @BindView(R.id.imageButton_play)
    ImageButton mImageButtonPlay;
    @BindView(R.id.progress_wheel)
    DonutProgress mProgressWheel;
    @BindView(R.id.image_roleplay_vioce)
    ImageView mImageRoleplayVioce;
    @BindView(R.id.tv_record_result)
    TextView mTvRecordResult;
    @BindView(R.id.simpleRatingBar_record_result)
    SimpleRatingBar mSimpleRatingBarRecordResult;

    private List<BreakThroughList> wordLists;
    private Mission mMission;
    private List<MissionItem> mWords;
    private int mViewPagerSelectedIndex = 0;
    private Follow mFollow;
    private RxPermissions mRxPermissions;
    private MissionItem mMissionItem;
    private List<Follow> mFollows = new ArrayList<>();
    private BreakThroughDetailPagerAdapter mPagerAdapter;

    @Override
    public void onInitData(Bundle savedInstanceState) {

        mImageButtonTry.setImageResource(R.mipmap.ic_breakthrough_try_disable);
        mImageButtonPlay.setTag(0);//0-暂停状态；1-播放
        mImageButtonTry.setTag(0);//0-暂停状态；1-播放
        mImageButtonTry.setEnabled(false);

        String imageUrl = "http://192.168.3.197:8080/U6Blearn.jpg";
        mWords = new ArrayList<>();
        mWords.add(new MissionItem(imageUrl, "http://192.168.3.197:8080/P055002.mp3", "Wow! What a great job!"));
        mWords.add(new MissionItem(imageUrl, "http://192.168.3.197:8080/P055003.mp3", "Yes. What other unusual jobs can you think of?"));
        mWords.add(new MissionItem(imageUrl, "http://192.168.3.197:8080/P055004.mp3", "How about a lion tamer?"));
        mWords.add(new MissionItem(imageUrl, "http://192.168.3.197:8080/P055005.mp3", "Oh, no. Too dangerous!"));
        mWords.add(new MissionItem(imageUrl, "http://192.168.3.197:8080/P055006(1).mp3", "What about a bee farmer?"));
        mMission = new Mission(1111, "Let's talk", 50, 100, mWords);

        if (ACacheMgr.getBreakThroughtFollow("kaibing111") != null) {//这里key由两部分组成，一个是username,另一个missionID

            mFollows = ACacheMgr.getBreakThroughtFollow("kaibing111");
        }

        mProgressBar.setMax(mWords.size());
        mProgressBar.setProgress(mFollows.size());
        mTvProgress.setText(mFollows.size() + "/" + mWords.size());
        FollowManager.getInstance(SysApplication.getApplication()).setmHandler(mHandler);
        FollowManager.getInstance(SysApplication.getApplication()).registerObserver(this);
        mRxPermissions = new RxPermissions(this);


        if (mFollows.size() == 0) {//此时应进行数据库的跟读查询
            pager.setIsToLedtScroll(false);
            mImageViewBack.setVisibility(View.INVISIBLE);
        } else {
            pager.setIsToLedtScroll(true);
            mImageViewBack.setVisibility(View.VISIBLE);
        }

        initFollow(0);


        mPagerAdapter = new BreakThroughDetailPagerAdapter(SysApplication.getApplication(), mWords, mFollows);
        pager.setOnPageChangeListener(this);
        pager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onInitView() {
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle(mMission.title);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_breakthrough_detail;
    }

    @OnClick(R.id.imageButton_play)
    public void play(View view) {
        if ((Integer) mImageButtonPlay.getTag() == 0) {//进行播放
            mImageButtonPlay.setTag(1);
            mImageButtonPlay.setImageResource(R.mipmap.ic_breakthrough_stop);
            MediaPlayerUtil.playFromIntenet(SysApplication.getApplication(), mWords.get(mViewPagerSelectedIndex).encryptSoundURL, null);
            MediaPlayerUtil.getInstance().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mImageButtonPlay.setImageResource(R.mipmap.ic_breakthrough_play);
                }
            });
        } else {//暂停音频播放
            mImageButtonPlay.setTag(0);
            mImageButtonPlay.setImageResource(R.mipmap.ic_breakthrough_play);
            MediaPlayerUtil.stop();
        }
    }

    /**
     * 回放自己的录音
     */
    @OnClick(R.id.imageButton_try)
    public void playBack() {
        if ((Integer) mImageButtonTry.getTag() == 0) {//进行播放
            mImageButtonTry.setTag(1);
            MediaPlayerUtil.playFromIntenet(SysApplication.getApplication(), mFollow.getRecordPath(), null);
        } else {//暂停音频播放
            mImageButtonTry.setTag(0);
            MediaPlayerUtil.stop();
        }
    }

    /**
     * 向后翻页
     *
     * @param view
     */
    @OnClick(R.id.imageView_back)
    public void goToBack(View view) {
        if (mViewPagerSelectedIndex == mWords.size()-1) {//查看成绩
            EventMessageBreakThroughResult eventMessageBreakThroughResult = new EventMessageBreakThroughResult();
            eventMessageBreakThroughResult.setFollows(mFollows);
            eventMessageBreakThroughResult.setTitle(mMission.title);
            EventBus.getDefault().postSticky(eventMessageBreakThroughResult);
            startActivity(new Intent(BreakThroughDetailActivity.this,BreakThroughResultActivity.class));
        } else {
            pager.setCurrentItem(mViewPagerSelectedIndex + 1);
        }
    }

    /**
     * 向前翻页
     *
     * @param view
     */
    @OnClick(R.id.imageView_forward)
    public void goToForward(View view) {
        pager.setCurrentItem(mViewPagerSelectedIndex - 1);
    }

    /**
     * 进行录音评测
     *
     * @param view
     */
    @OnClick(R.id.image_roleplay_vioce)
    public void recording(View view) {
        mImageButtonPlay.setEnabled(false);
        mImageButtonPlay.setImageResource(R.mipmap.ic_breakthrough_play_disable);
        mImageButtonTry.setEnabled(false);
        mImageButtonTry.setImageResource(R.mipmap.ic_breakthrough_try_disable);

        if (mFollow.getrState() == FollowManager.STATE_NONE) {
            Log.e("pRecord--->State", FollowManager.STATE_NONE + "");
            mRxPermissions.request(Manifest.permission.RECORD_AUDIO).subscribe(new Observer<Boolean>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull Boolean aBoolean) {
                    if (aBoolean) {
                        FollowManager.getInstance(SysApplication.getApplication()).recording(mFollow);
                    } else {
                        Toast.makeText(SysApplication.getApplication(), "权限阻止", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {


        if (position == 0) {//第一页，不显示向前翻页的按钮
            mImageViewForward.setVisibility(View.INVISIBLE);
        } else {//不在第一页，显示向前翻页的按钮
            mImageViewForward.setVisibility(View.VISIBLE);
        }

        mViewPagerSelectedIndex = position;

        mImageButtonPlay.setTag(0);
        mImageButtonPlay.setImageResource(R.mipmap.ic_breakthrough_play);
        MediaPlayerUtil.stop();

        FollowManager.getInstance(SysApplication.getApplication()).stop(true);

        //        Log.e("onPageSelected","position="+position);
        initFollow(position);
    }

    /**
     * 初始化跟读模块word的数据
     */
    private void initFollow(int position) {
        if (mFollows.size() <= position) {
            mFollow = new Follow();
            mMissionItem = mWords.get(mViewPagerSelectedIndex);
            mFollow.setParentID(mMission.missionID);
            mFollow.setText(mMissionItem.word);
            mFollow.setSound(mMissionItem.encryptSoundURL);

            mFollows.add(mFollow);

            mTvRecordResult.setVisibility(View.INVISIBLE);
            mSimpleRatingBarRecordResult.setVisibility(View.INVISIBLE);
            mImageButtonTry.setEnabled(false);
            mImageButtonTry.setImageResource(R.mipmap.ic_breakthrough_try_disable);

            pager.setIsToLedtScroll(false);//禁止向左滑动到下一个
            mImageViewBack.setVisibility(View.INVISIBLE);

        } else {
            mFollow = mFollows.get(position);
            //
            if (!TextUtils.isEmpty(mFollow.getRecordPath())) {//已进行过录音评测
                float score = mFollow.getScore();
                mTvRecordResult.setVisibility(View.VISIBLE);
                setTvRecordResult(score);

                mSimpleRatingBarRecordResult.setVisibility(View.VISIBLE);
                mSimpleRatingBarRecordResult.setNumberOfStars(5);
                mSimpleRatingBarRecordResult.setRating((score / 100) * 5);

                mImageButtonTry.setEnabled(true);
                mImageButtonTry.setTag(0);
                mImageButtonTry.setImageResource(R.mipmap.ic_breakthrough_try);

                pager.setIsToLedtScroll(true);//解除禁止，可滑动到下一个
                mImageViewBack.setVisibility(View.VISIBLE);
            } else {
                mTvRecordResult.setVisibility(View.INVISIBLE);
                mSimpleRatingBarRecordResult.setVisibility(View.INVISIBLE);
                mImageButtonTry.setEnabled(false);
                mImageButtonTry.setImageResource(R.mipmap.ic_breakthrough_try_disable);

                pager.setIsToLedtScroll(false);//禁止向左滑动到下一个
                mImageViewBack.setVisibility(View.INVISIBLE);
            }

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    android.os.Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case C.FOLLOW_SHOW_EVALUATE_TIPS:
                    mTvRecordResult.setVisibility(View.VISIBLE);
                    mTvRecordResult.setText("正在测评中");
                    break;
                case C.FOLLOW_CANCLE_EVALUATE_TIPS://完成测评
                    mFollow = (Follow) msg.obj;

                    ACacheMgr.saveBreakThroughtFollow("kaibing111", mFollows);//跟读数据进行保存

                    //                    mViewPagerSelectedIndex++;
                    int size = mFollows.size() < mWords.size() ? mFollows.size() : mWords.size();
                    mImageViewBack.setVisibility(View.VISIBLE);//向后翻页的箭头可见
                    pager.setIsToLedtScroll(true);

                    mProgressBar.setProgress(size);
                    mTvProgress.setText(size + "/" + mWords.size());

                    mPagerAdapter.setWordResultColor(mFollows);
                    mPagerAdapter.notifyDataSetChanged();

                    float score = mFollow.getScore();
                    setTvRecordResult(score);
                    playScoreAudio(score);
                    mImageButtonTry.setImageResource(R.mipmap.ic_breakthrough_try);
                    mImageButtonTry.setEnabled(true);
                    mImageButtonPlay.setEnabled(true);
                    mImageButtonPlay.setImageResource(R.mipmap.ic_breakthrough_play);
                    mSimpleRatingBarRecordResult.setVisibility(View.VISIBLE);
                    mSimpleRatingBarRecordResult.setNumberOfStars(5);
                    mSimpleRatingBarRecordResult.setRating((score / 100) * 5);
                    break;

                case C.FOLLOW_EVALUATE_ERROR:
                    mTvRecordResult.setVisibility(View.INVISIBLE);
                    VoiceEvaluate.onlineSDKError(SysApplication.getApplication(), (SDKError) msg.obj);
                    break;
            }
        }
    };

    /**
     * 根据测评评语分数朗读对应的鼓励音频
     *
     * @param score
     */
    private void playScoreAudio(float score) {

        int result = (int) score;
        String scoreAudioPath = "follow/follow_86_100.mp3";
        if (result < 60) {
            scoreAudioPath = "follow/follow_below_60.mp3";
        } else if (result < 69) {
            scoreAudioPath = "follow/follow_60_69.mp3";
        } else if (result < 85) {
            scoreAudioPath = "follow/follow_60_69.mp3";
        } else if (result <= 100) {
            scoreAudioPath = "follow/follow_86_100.mp3";
        }
        com.king.reading.common.utils.MediaPlayerUtil.play(SysApplication.getApplication(), scoreAudioPath);
    }

    /**
     * 设置跟读测评评语
     *
     * @param score
     */
    private void setTvRecordResult(float score) {
        int result = (int) score;
        if (result < 60) {
            mTvRecordResult.setText("要加把劲哦~");
        } else if (result < 69) {
            mTvRecordResult.setText("还不错哦！");
        } else if (result < 85) {
            mTvRecordResult.setText("还不错哦！");
        } else if (result <= 100) {
            mTvRecordResult.setText("好棒啊！");
        }
    }

    @Override
    public void onStateChanged(final Follow follow) {
        mProgressWheel.post(new Runnable() {
            @Override
            public void run() {
                setRecordProgress(follow);
            }


        });
    }


    @Override
    public void onProgressed(final Follow follow) {
        mProgressWheel.post(new Runnable() {
            @Override
            public void run() {
                setRecordProgress(follow);
            }
        });
    }

    private void setRecordProgress(Follow follow) {
        if (mProgressWheel.getMax() != follow.getrMaxProgress()) {
            mProgressWheel.setMax(follow.getrMaxProgress());
        }
        if (follow.getrState() == FollowManager.STATE_NONE) {
            follow.setrCurProgress(0);
            mImageRoleplayVioce.setBackgroundResource(R.mipmap.ic_roleplay_follow_reading);
        } else if (follow.getrState() == FollowManager.STATE_LOADING) {
            mImageRoleplayVioce.setBackgroundResource(R.mipmap.ic_roleplay_follow_reading);
        }
        mProgressWheel.setProgress(follow.getrCurProgress());
    }


}
