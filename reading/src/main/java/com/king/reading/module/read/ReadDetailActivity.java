package com.king.reading.module.read;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CheckableImageButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.adapter.ReadingPagerAdapter;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.common.utils.AppScreenMgr;
import com.king.reading.common.utils.ToastUtils;
import com.king.reading.data.entities.PageEntity;
import com.king.reading.data.repository.ResRepository;
import com.king.reading.ddb.SecKeyPair;
import com.king.reading.encyption.glide.EncryptionFileToStreamDecoder;
import com.king.reading.model.ReadingDetailOutline;
import com.king.reading.widget.PageLayout;
import com.king.reading.widget.ReadingViewPager;
import com.king.reading.widget.drawer.Drawer;
import com.king.reading.widget.drawer.DrawerBuilder;
import com.king.reading.widget.drawer.model.interfaces.IDrawerItem;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.Lazy;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.vov.vitamio.MediaPlayerUtil;


/**
 * Created by hu.yang on 2017/6/12.
 */

@Route(path = C.ROUTER_READ_DETAIL)
public class ReadDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnTouchListener {
    @Inject
    ResRepository repository;
    @Inject
    Lazy<EncryptionFileToStreamDecoder> decoder;
    public static final int MODULE = 1;
    public static final int UNIT = 2;
    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.image_reading_repeat)
    CheckableImageButton mImageReadingRepeat;
    @BindView(R.id.tv_reading_repeat)
    TextView mTvReadingRepeat;
    @BindView(R.id.image_listen_playAndStop)
    CheckableImageButton mImageListenPlayAndStop;
    @BindView(R.id.image_reading_translate)
    CheckableImageButton mImageReadingTranslate;
    @BindView(R.id.tv_reading_translate)
    TextView mTvReadingTranslate;
    @BindView(R.id.tv_reading_detail_top)
    TextView mTvReadingDetailTop;
    @BindView(R.id.constraintLayout)
    ConstraintLayout mConstraintLayout;
    @BindView(R.id.pager_reading_detail_gallery)
    ReadingViewPager mReadingViewPager;
    @Autowired
    public String start;
    @Autowired
    public String end;
    @Autowired
    public String current;

    private ViewGroup mNavigationView;
    private ViewGroup mContentView;
    private Drawer result;
    private List<ReadingDetailOutline> list = new ArrayList<>();
    private boolean mIsTranslatingOpened;
    private ReadingPagerAdapter mAdapter;
    private PageLayout mCurrentPage;
    private View mCurrentView;
    private float mDownX;
    public static boolean mIsRepeateImageButtonchecked;
    public static boolean mIsStartWordSelected;
    public static boolean mIsEndWordsSelected;
    public static int mStartPageIndex = -1;
    public static int mEndPageIndex = -1;
    private int mCurrent;

    @Override
    public void onInitData(Bundle savedInstanceState) {
        setCenterTitle("Unit1");
        setLeftImageIcon(R.mipmap.ic_back);
        setRightText("目录");
        getAppComponent().plus().inject(this);
        ARouter.getInstance().inject(this);
    }

    @Override
    public void onInitView() {
        mImageListenPlayAndStop.setTag(false);
        mReadingViewPager.setOnPageChangeListener(this);
        mReadingViewPager.setOnTouchListener(this);
        result = new DrawerBuilder()
                .withActivity(this)
                .withDrawerGravity(GravityCompat.END)
                .withSliderBackgroundColorRes(R.color.white)
                .withHeader(R.layout.draw_reading_detail_header)
                .withHeaderDivider(false)
                .withTranslucentStatusBar(true)
                .addDrawerItems(
                        new OutlineModuleDrawerItem().withContent("Module1"),
                        new OutlineUnitDrawerItem().withContent("unit1").withSelectedColorRes(R.color.reading_detail_outline_selected_bg).withSelectedTextColor(getResources().getColor(R.color.reading_detail_outline_unit_text)),
                        new OutlineUnitDrawerItem().withContent("unit2").withSelectedColorRes(R.color.reading_detail_outline_selected_bg).withSelectedTextColor(getResources().getColor(R.color.reading_detail_outline_unit_text)),
                        new OutlineModuleDrawerItem().withContent("Module2"),
                        new OutlineUnitDrawerItem().withContent("unit3").withSelectedColorRes(R.color.reading_detail_outline_selected_bg).withSelectedTextColor(getResources().getColor(R.color.reading_detail_outline_unit_text)),
                        new OutlineUnitDrawerItem().withContent("unit4").withSelectedColorRes(R.color.reading_detail_outline_selected_bg).withSelectedTextColor(getResources().getColor(R.color.reading_detail_outline_unit_text))

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        ToastUtils.show("点击了"+position);
                        return false;
                    }
                })
                .build();
        solutionDrawerStatusBar();
        repository.getSecKey().subscribe(new Consumer<SecKeyPair>() {
            @Override
            public void accept(@NonNull final SecKeyPair secKeyPair) throws Exception {
                decoder.get().setSecretKey(secKeyPair.secKey);
                repository.getRangePages(secKeyPair.resourceID, Integer.parseInt(start), Integer.parseInt(end)).subscribe(new Consumer<List<PageEntity>>() {
                    @Override
                    public void accept(@NonNull List<PageEntity> pageEntities) throws Exception {
                        initViewPagerData(pageEntities);
                    }
                });
            }
        });

    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
        // 接收单词翻译的信息
        mTvReadingDetailTop.setText(event);
    }


    /**
     * 播放或者暂停
     *
     * @param v
     */
    @OnClick(R.id.image_listen_playAndStop)
    public void playOrStop(View v) {
        /*if (mStartPageIndex > mEndPageIndex) {
            Toast.makeText(SysApplication.getApplication(), "仅支持往后跟读哦", Toast.LENGTH_SHORT).show();
            return;
        }*/

        if (!(Boolean) v.getTag()) {
            playAllPager(v);
        } else {
            stopAllPager(v);
        }
    }

    @OnClick(R.id.image_reading_repeat)
    public void readingRepeat(View v) {
        resetAutoPlay();
        mIsRepeateImageButtonchecked = !mIsRepeateImageButtonchecked;
        mImageReadingRepeat.setChecked(mIsRepeateImageButtonchecked);
        if (mIsRepeateImageButtonchecked) {//开始复读，选择起始句和结束句
            if (!mIsStartWordSelected) {
                mTvReadingDetailTop.setText("请点击选择开始句");
            }
        } else {//复读清除
            mIsStartWordSelected = false;
            mIsEndWordsSelected = false;
            mTvReadingDetailTop.setText("请点击选择点读句");
        }
    }

    @OnClick(R.id.image_reading_translate)
    public void readingTranslate(View v) {
        mIsTranslatingOpened = !mIsTranslatingOpened;
        if (mIsTranslatingOpened) {
            mTvReadingDetailTop.setVisibility(View.VISIBLE);
            //            mTvReadingDetailTop.setBackgroundColor(getResources().getColor(R.color.read_translate_bg));
        } else {
            mTvReadingDetailTop.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 初始化viewPager
     *
     * @param getPageResponse
     */
    private void initViewPagerData(List<PageEntity> getPageResponse) {

        mAdapter = new ReadingPagerAdapter(this, getPageResponse, mReadingViewPager, decoder.get(), mHandler);
        mReadingViewPager.setAdapter(mAdapter);
        mReadingViewPager.setCurrentItem(Integer.parseInt(current)-Integer.parseInt(start));
    }

    @Override
    public int getContentView() {
        return R.layout.activity_reading_detail;
    }


    Handler mHandler = new Handler() {

        private int mTotal;


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case C.HANDLE_AUTO_READING_ALL_PAGE:
                    // 自动全文播放
                    mCurrent = mReadingViewPager.getCurrentItem();
                    mTotal = mAdapter.getCount();
                    mCurrentView = mReadingViewPager.findViewById(mReadingViewPager.getCurrentItem());
                    if (mCurrentView != null) {
                        mCurrentPage = (PageLayout) mCurrentView.getTag();
                        mCurrentPage.autoReading(mCurrent, mTotal);
                    }
                    break;
                case C.HANDLE_READING_TURN_PAGE:
                    // 自动向后翻页
                    if (mReadingViewPager.getCurrentItem() < mAdapter.getCount() - 1) {
                        //回收整个页面产生的bitmap
                        View currentView1 = mReadingViewPager.findViewById(mReadingViewPager.getCurrentItem());
                        if (currentView1 != null) {
                            mCurrentPage = (PageLayout) currentView1.getTag();
                            recyclePageBitmap(mCurrentPage);
                        }
                        mReadingViewPager.setCurrentItem(mReadingViewPager.getCurrentItem() + 1, true);
                    } else {
                        resetAutoPlay();
                    }
                    break;
                case C.HANDLE_INTERRUPT_READING_ALL_PAGE:
                    // 全文跟读被中断时，全文播放按钮恢复初始状态
                    if ((Boolean) mImageListenPlayAndStop.getTag()) {
                        mImageListenPlayAndStop.setTag(false);
                        mImageListenPlayAndStop.setChecked(false);
                        mHandler.removeMessages(C.HANDLE_AUTO_READING_ALL_PAGE);
                        mHandler.removeMessages(C.HANDLE_READING_TURN_PAGE);
                    }
                    break;
                case C.HANDLE_SELECTED_START_WORD_REPEAT_MODEL:
                    mTvReadingDetailTop.setText("请点击选择结束句");
                    break;
                case C.HANDLE_REPEATE_MODE_READING:
                    if (mStartPageIndex==mCurrent) {//复读的开始句和结束句在同一页
                        mHandler.sendEmptyMessage(C.HANDLE_AUTO_READING_ALL_PAGE);
                    } else {
                        mReadingViewPager.setCurrentItem(mStartPageIndex);
                    }
                    /*mCurrent = mReadingViewPager.getCurrentItem();
                    mTotal = mAdapter.getCount();
                    mCurrentView = mReadingViewPager.findViewById(mStartPageIndex);
                    if (mCurrentView != null) {
                        mCurrentPage = (PageLayout) mCurrentView.getTag();
                        mCurrentPage.autoReading(mCurrent, mTotal);
                    }*/
                    break;
            }
        }
    };

    /**
     * 读到最后一页，暂停播放
     */
    private void resetAutoPlay() {
        MediaPlayerUtil.stop();
        if ((Boolean) mImageListenPlayAndStop.getTag() || !PageLayout.mIsReadingOnclickFinished) {
            mImageListenPlayAndStop.setTag(false);
            mImageListenPlayAndStop.setChecked(false);
            mHandler.removeMessages(C.HANDLE_AUTO_READING_ALL_PAGE);
            mHandler.removeMessages(C.HANDLE_READING_TURN_PAGE);
            mCurrentPage = getCurrentPageLayoutView();
            mCurrentPage.removeFrame();
        }
    }

    /**
     * 回收页面产生的bitmap
     *
     * @param currentPage
     */
    private void recyclePageBitmap(PageLayout currentPage) {
        if (currentPage.mReadingBgBitmap != null && !currentPage.mReadingBgBitmap.isRecycled()) {
            currentPage.mReadingBgBitmap.recycle();
            currentPage.mReadingBgBitmap = null;
        }
        System.gc();
    }


    private void stopAllPager(View v) {
        mImageListenPlayAndStop.setChecked(false);
        v.setTag(false);
        mHandler.removeMessages(C.HANDLE_AUTO_READING_ALL_PAGE);
        mHandler.removeMessages(C.HANDLE_READING_TURN_PAGE);
        MediaPlayerUtil.stop();
        if (mCurrentPage != null) {
            mCurrentPage.removeFrame();
        }
    }

    /**
     * 全文播放或者是复读播放
     *
     * @param v
     */
    private void playAllPager(View v) {
        mImageListenPlayAndStop.setChecked(true);
        v.setTag(true);
        if (mIsStartWordSelected) {//复读播放
            mHandler.sendEmptyMessage(C.HANDLE_REPEATE_MODE_READING);
        } else {
            mHandler.sendEmptyMessage(C.HANDLE_AUTO_READING_ALL_PAGE);
        }
    }


    private void solutionDrawerStatusBar() {
        mNavigationView = (ViewGroup) result.getDrawerLayout().getChildAt(1);
        //获取侧滑栏的RecyclerView
        View childAt = mNavigationView.getChildAt(1);

        //创建一个View，并把它的高度设为状态栏高度，并添加到侧滑栏中
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, AppScreenMgr.getStatusHeight(getApplicationContext()));
        View view = new View(getApplicationContext());
        view.setBackgroundResource(R.color.colorPrimary);
        view.setId(R.id.material_statusBar_id);
        view.setLayoutParams(params);
        mNavigationView.addView(view);

        //把RecyclerView设置为View的下面
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) childAt.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.material_statusBar_id);
        childAt.setLayoutParams(layoutParams);
        mContentView = (ViewGroup) result.getDrawerLayout().getChildAt(0);
    }

    @Override
    public boolean hasEventBus() {
        return true;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // 翻页完成操作时解除全文跟读键的屏蔽、取消红框、停止声音
        mCurrent = mReadingViewPager.getCurrentItem();

        mCurrentView = mReadingViewPager.findViewById(mReadingViewPager.getCurrentItem());
        if (mCurrentView != null) {
            mCurrentPage = (PageLayout) mCurrentView.getTag();
            mCurrentPage.reset();
        }

        if ((Boolean) mImageListenPlayAndStop.getTag()) {
            mHandler.sendEmptyMessageDelayed(C.HANDLE_AUTO_READING_ALL_PAGE, 1000);
        }
    }

    /**
     * 得到当前的PageLayout
     *
     * @return
     */
    public PageLayout getCurrentPageLayoutView() {
        mCurrentView = mReadingViewPager.findViewById(mReadingViewPager.getCurrentItem());
        if (mCurrentView != null) {
            mCurrentPage = (PageLayout) mCurrentView.getTag();
        }
        return mCurrentPage;
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 1) {
            // 正在滑动时
            // 屏蔽全文跟读按钮
            mImageListenPlayAndStop.setEnabled(false);
        } else {
            mImageListenPlayAndStop.setEnabled(true);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(event.getX() - mDownX) > 10) {
                    // 滑动时
                    resetAutoPlay();
                }
                break;
            default:
                break;
        }
        return false;
    }
}
