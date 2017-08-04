package com.king.reading.module.learn.breakthrough;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.common.adapter.util.SpaceItemDecoration;
import com.king.reading.common.utils.MediaPlayerUtil;
import com.king.reading.model.BreakThroughResult;
import com.king.reading.model.EventMessageBreakThroughResult;
import com.king.reading.model.Follow;
import com.king.reading.widget.SimpleRatingBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.king.reading.R.id.progress_breakthrough_score;

/**
 * Created by hu.yang on 2017/6/13.
 */

public class BreakThroughResultActivity extends BaseActivity {
    @BindView(R.id.recycle_breakthrough_score_detail)
    RecyclerView recyclerView;

    ImageView mImageBreakthroughTextPrompt;
    ImageView mImageViewTips;
    SimpleRatingBar mSimpleRatingBar;
    TextView mTvBreakthroughTotalScore;
    ProgressBar mProgressBreakthroughScore;
    /*@BindView(R.id.tv_examine_totalScore) TextView score;
    @BindView(R.id.progress_examine_score) ProgressBar bar;*/

    private List<BreakThroughResult.ScoreDetail> results;
    private float mScore;
    private List<Follow> mFollows;
    private String mTitle;
    private int mRateNumber;

    @Override
    public void onInitData(Bundle savedInstanceState) {

    }

    @Override
    public void onInitView() {
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMessageBreakThroughResult eventMessageBreakThroughResult) {
        mFollows = eventMessageBreakThroughResult.getFollows();
        mTitle = eventMessageBreakThroughResult.getTitle();

        setFollowData();
        initView();
//        initBannerData();

    }


    private void initView() {
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle(mTitle);

        ExamineResultAdapter resultAdapter = new ExamineResultAdapter(R.layout.item_breakthrough_score, results);
        ViewGroup bannerHeader = (ViewGroup) getLayoutInflater().inflate(R.layout.item_header_breakthrough_result_banner, null);
        mImageBreakthroughTextPrompt = (ImageView) bannerHeader.findViewById(R.id.image_breakthrough_text_prompt);
        mImageViewTips = (ImageView) bannerHeader.findViewById(R.id.imageView_tips);
        mSimpleRatingBar = (SimpleRatingBar) bannerHeader.findViewById(R.id.simpleRatingBar);
        mTvBreakthroughTotalScore = (TextView) bannerHeader.findViewById(R.id.tv_breakthrough_totalScore);
        mProgressBreakthroughScore = (ProgressBar) bannerHeader.findViewById(progress_breakthrough_score);
        initBannerData();

        resultAdapter.addHeaderView(bannerHeader);
        recyclerView.addItemDecoration(new SpaceItemDecoration(32));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(resultAdapter);
    }

    private void initBannerData() {
        getBreakThroughtScore(mFollows);

        setBannerHeaderScore();

    }

    /**
     * 设置跟读每一句的文本和星星数
     */
    private void setFollowData() {
        results = new ArrayList<>();
        for (Follow follow : mFollows) {
            String word = follow.getText();
            int score = (int) follow.getScore();
            BreakThroughResult.ScoreDetail scoreDetail = new BreakThroughResult.ScoreDetail(word, getRateScore(score));
            results.add(scoreDetail);
        }
    }


    /**
     * 拿到闯关的平均分数，60分以上，闯关成功
     *
     * @param follows
     */
    private void getBreakThroughtScore(List<Follow> follows) {
        mScore = 0;
        for (Follow follow : follows) {
            mScore += follow.getScore();
            mRateNumber += getRateScore((int)follow.getScore());
        }
        mScore = mScore / follows.size();
    }

    private void setBannerHeaderScore() {
        if (mScore < 60) {//两颗星，闯关失败
            mImageBreakthroughTextPrompt.setImageResource(R.mipmap.ic_roleplay_c);
            mImageViewTips.setImageResource(R.mipmap.tv_breakthrough_result_fail);
            mSimpleRatingBar.setRating(2);

            MediaPlayerUtil.play(SysApplication.getApplication(), "follow/breakthrough_failed.mp3");
        } else if (mScore < 70) {//三颗星
            mImageBreakthroughTextPrompt.setImageResource(R.mipmap.ic_roleplay_b);
            mImageViewTips.setImageResource(R.mipmap.tv_breakthrough_result_ok);
            mSimpleRatingBar.setRating(3);
            MediaPlayerUtil.play(SysApplication.getApplication(), "follow/breakthrough_success.mp3");
        } else if (mScore < 86) {//四颗星
            mImageBreakthroughTextPrompt.setImageResource(R.mipmap.ic_roleplay_b);
            mImageViewTips.setImageResource(R.mipmap.tv_breakthrough_result_ok);
            mSimpleRatingBar.setRating(4);
            MediaPlayerUtil.play(SysApplication.getApplication(), "follow/breakthrough_success.mp3");
        } else {//五颗星
            mImageBreakthroughTextPrompt.setImageResource(R.mipmap.ic_roleplay_a);
            mImageViewTips.setImageResource(R.mipmap.tv_breakthrough_result_ok);
            mSimpleRatingBar.setRating(5);
            MediaPlayerUtil.play(SysApplication.getApplication(), "follow/breakthrough_success.mp3");
        }


        mTvBreakthroughTotalScore.setText("x" + mRateNumber);
        mProgressBreakthroughScore.setProgress((int) mScore);
    }

    /**
     * 根据分数返回星星数
     * @param score
     * @return
     */
    private int getRateScore(int score) {
        if (score < 60) {//两颗星，闯关失败
            return 2;
        } else if (score < 70) {//三颗星
            return 3;

        } else if (score < 86) {//四颗星
            return 4;
        } else {//五颗星
            return 5;

        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_breakthrough_result;
    }


    private class ExamineResultAdapter extends BaseQuickAdapter<BreakThroughResult.ScoreDetail, BaseViewHolder> {
        public ExamineResultAdapter(@LayoutRes int layoutResId, @Nullable List<BreakThroughResult.ScoreDetail> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, BreakThroughResult.ScoreDetail item) {
            helper.setText(R.id.tv_breakthrough_score_num, item.score + "");
            helper.setText(R.id.tv_breakthrough_score_word, item.word);
        }

        @Override
        public boolean isHeaderViewAsFlow() {
            return super.isHeaderViewAsFlow();
        }
    }

    @Override
    public boolean hasEventBus() {
        return true;
    }
}
