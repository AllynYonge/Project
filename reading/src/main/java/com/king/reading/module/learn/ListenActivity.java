package com.king.reading.module.learn;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CheckableImageButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.EmptyUtils;
import com.google.common.collect.Lists;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.adapter.ListenTimerAdapter;
import com.king.reading.base.activity.RecyclerViewActivity;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseExpandableAnimAdapter;
import com.king.reading.common.adapter.BaseLinearGridAdapter;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.data.entities.BookEntity;
import com.king.reading.data.entities.PageEntity;
import com.king.reading.data.entities.WordEntity;
import com.king.reading.data.repository.ResRepository;
import com.king.reading.ddb.Line;
import com.king.reading.model.ListenModule;
import com.king.reading.model.ListenUnit;
import com.king.reading.model.ViewMappers;
import com.king.reading.widget.PopWindowListemTimer;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayerUtil;

/**
 * Created by hu.yang on 2017/6/9.
 */

@Route(path = C.ROUTER_LEARN_LISTEN)
public class ListenActivity extends RecyclerViewActivity<ListenModule> {
    @Inject
    ResRepository mResRepository;
    private List<ListenModule> mGroupModules = Lists.newArrayList();

    @BindView(R.id.image_listen_playAndStop)
    CheckableImageButton playToggle;
    private List<ListenUnit> mSelectedSecondaries = new ArrayList<>();
    private int mCurrentPage;//当前页码
    private int mCurrentSecondaryIndex;// 当前二级目录序号
    private int mCurrentSoundIndex = 1;//当前声音序号
    private ListenUnit mCurrentSecondary;
    private List<Line> mLines = new ArrayList<>();
    private TextView tMode;
    private PopWindowListemTimer mPopWindowListemTimer;
    private GridView mGdListemTimer;
    private ListenTimerAdapter mListenTimerAdapter;
    private int mListenTimerTag = 0;//0-无定时，1-定时10分钟，2-定时20分钟....6-定时60分钟
    private Timer mListenTimer;
    private int MLISTENTIMERTYPEZERO = 0;//0-无定时
    private int MLISTENTIMERTYPEONE = 10;//1-10分钟
    private int MLISTENTIMERTYPETWO = 20;//2-20分钟
    private int MLISTENTIMERTYPETHREE = 30;//3-30分钟
    private int MLISTENTIMERTYPEFOUR = 40;//4-40分钟
    private int MLISTENTIMERTYPEFIVE = 50;//5-50分钟
    private int MLISTENTIMERTYPESIX = 60;//6-60分钟
    private int mCurrenListenTimerCount = 0;
    private Timer mTimer;
    private long bookId;


    @Override
    public void onInitData(Bundle savedInstanceState) {
        getAppComponent().plus().inject(this);
    }

    @OnClick(R.id.image_listen_playAndStop)
    public void playToggle(View view) {
        if (mSelectedSecondaries.size() <= 0) {
            Toast.makeText(SysApplication.getApplication(), "请选择播放单元哦", Toast.LENGTH_SHORT).show();
            return;
        }

        playToggle.setChecked(!playToggle.isChecked());
        //        getRecyclerAdapter().notifyDataSetChanged();

        // 开启全文听
        if ((Integer) view.getTag() == 0) {
            // 播放中
            view.setTag(1);
            mHandler.sendEmptyMessage(C.LISTEN_GET_SECONDARY);
            setSecondaryPlaying(mCurrentSecondaryIndex, false);
            /*if (mListenTimerTag != 0) {
                startListenTimer();
            }*/
        } else if ((Integer) view.getTag() == 1) {
            // 暂停
            view.setTag(2);
            MediaPlayerUtil.pause();
            setSecondaryPlaying(mCurrentSecondaryIndex, true);
            //            cancleListenTimer();
        } else {
            // 继续
            view.setTag(1);
            //下面这种情况可以保证按照上次暂停的地方继续朗读，不然得在当前的二级目录下，重头开始播放
            /*if (MediaPlayerUtil.getInstance().getCurrentPosition() > 0) {
                MediaPlayerUtil.start();
            } else {*/
            mHandler.sendEmptyMessage(C.LISTEN_GET_SECONDARY);
            setSecondaryPlaying(mCurrentSecondaryIndex, false);
            //            startListenTimer();
        }
    }

    @Override
    public void onInitView() {
        super.onInitView();
        playToggle.setTag(0);// 0-默认状态，1-播放中，2-暂停
        getRightImage().setTag(0);//0-默认单词播放，1-循环播放
        playToggle.setChecked(false);
        setCenterTitle("三年级上册");
        setLeftImageIcon(R.mipmap.ic_avatar);
        setRightAssistImageIcon(R.mipmap.ic_alwayslisten_timing);
        setRightImageIcon(R.mipmap.ic_alwayslisten_sequens);
        // 获取TelephonyManager
        TelephonyManager phoneManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onRightClick(View v) {

        if ((Integer) getRightImage().getTag() == 0) {
            getRightImage().setTag(1);
            Toast.makeText(SysApplication.getApplication(), "循环播放", Toast.LENGTH_SHORT).show();
            setRightImageIcon(R.mipmap.ic_alwayslisten_loop);
        } else if ((Integer) getRightImage().getTag() == 1) {
            getRightImage().setTag(0);
            Toast.makeText(SysApplication.getApplication(), "单次播放", Toast.LENGTH_SHORT).show();
            setRightImageIcon(R.mipmap.ic_alwayslisten_sequens);

        }
    }

    @Override
    public void onRightLeftclick(View v) {
        Toast.makeText(SysApplication.getApplication(), "时间设置设定", Toast.LENGTH_SHORT).show();
        setPopTimer();
    }

    @Override
    public BaseQuickAdapter<ListenModule, BaseViewHolder> getAdapter() {
        return new BaseExpandableAnimAdapter<ListenModule>(R.layout.item_listen_textbook, mGroupModules, BaseLinearGridAdapter.VERTICAL) {
            @Override
            protected int getChildLayoutRes() {
                return R.layout.item_listen_textbook_unit;
            }

            @Override
            protected int getToggleExpandId() {
                return R.id.constraint_listen_textbook_item;
            }

            @Override
            protected void bindChildData(ViewGroup childHolder, final ListenModule group, int index) {
                final ListenUnit item = group.getItems().get(index);
                TextView unitTitle = (TextView) childHolder.findViewById(R.id.tv_listen_unit_unitTitle);
                TextView pageNumber = (TextView) childHolder.findViewById(R.id.tv_listen_unit_pageNumber);
                ImageView playing = (ImageView) childHolder.findViewById(R.id.image_listen_playing);
                final AppCompatCheckBox checkBox = (AppCompatCheckBox) childHolder.findViewById(R.id.chk_listen_unit_selected);
                unitTitle.setText(item.title);
                pageNumber.setText(item.pageNumber);

                if (item.isPlaying()) {
                    if (!item.isPause()) {
                        playing.setVisibility(View.VISIBLE);
                        AnimationDrawable drawable = (AnimationDrawable) playing.getDrawable();
                        drawable.start();

                    } else {
                        playing.setVisibility(View.VISIBLE);
                        AnimationDrawable drawable = (AnimationDrawable) playing.getDrawable();
                        drawable.stop();

                    }
                } else {
                    playing.setVisibility(View.GONE);
                    AnimationDrawable drawable = (AnimationDrawable) playing.getDrawable();
                    drawable.stop();
                }

                checkBox.setChecked(item.isChecked());
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.setChecked(checkBox.isChecked());
                        group.setChecked(true);
                        for (ListenUnit unit : group.getItems()) {
                            if (!unit.isChecked()) {
                                group.setChecked(false);
                                break;
                            }
                        }
                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                                mHandler.sendEmptyMessage(C.LISTEN_STOP_PLAY_SOUND);
                                mHandler.sendEmptyMessage(C.LISTEN_GET_SELECTED);
                            }
                        });
                    }
                });
            }

            @Override
            protected void convert(BaseViewHolder helper, final ListenModule item) {
                super.convert(helper, item);
                helper.setText(R.id.tv_listen_textbook_module, item.moduleName);
                helper.setText(R.id.tv_listen_textbook_pageNumber, item.modulePageNumber);
                helper.setChecked(R.id.chk_listen_textbook_selected, item.isChecked());
                helper.addOnClickListener(R.id.chk_listen_textbook_selected);
            }
        };
    }

    @Override
    public int getContentView() {
        return R.layout.activity_listen;
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case C.LISTEN_GET_SECONDARY:
                    // 得到二级目录对象
                    if ((Integer) playToggle.getTag() != 1 || mCurrentSecondaryIndex >= mSelectedSecondaries.size())
                        break;
                    mCurrentSecondary = mSelectedSecondaries.get(mCurrentSecondaryIndex);
                    if (mCurrentPage <= mCurrentSecondary.getStartPage()) {
                        // 当前二级标题与上一个二级标题不存在页码重合
                        mCurrentPage = mCurrentSecondary.getStartPage();

                        mHandler.sendEmptyMessage(C.LISTEN_GET_READING_CONFIG);
                    } else {
                        // 当前二级标题与上一个二级标题存在页码重合
                        if (mCurrentPage <= mCurrentSecondary.getEndPage())
                            mHandler.sendEmptyMessage(C.LISTEN_GET_READING_CONFIG);
                        else {
                            mCurrentSecondaryIndex++;
                            mHandler.sendEmptyMessage(C.LISTEN_GET_SECONDARY);
                        }
                    }
                    break;
                case C.LISTEN_GET_READING_CONFIG:
                    // 获取当前页配置数据
                    if ((Integer) playToggle.getTag() != 1)
                        break;
                    mLines.clear();
                    //根据mCurrentPage,请求网络，获取当页的配置信息
                    mResRepository.getRangePages(1, 2, 3).subscribe(new Consumer<List<PageEntity>>() {
                        @Override
                        public void accept(@NonNull List<PageEntity> pageEntities) throws Exception {
                            mLines = pageEntities.get(0).page.lines;

                            //这是异步操作，是个耗时的操作
                            // if (configPath != null) {
                            if (mLines.size() > 0) {
                                // 当前页有点读内容
                                mCurrentSoundIndex = 0;
                                mHandler.sendEmptyMessage(C.LISTEN_PLAY_SELECTED_SOUND);
                            } else {
                                // 当前页无点读内容,进入下一页
                                mCurrentPage++;
                                if (mCurrentPage <= mCurrentSecondary.getEndPage()) {
                                    // 二级目录中下一页
                                    mHandler.sendEmptyMessage(C.LISTEN_GET_READING_CONFIG);
                                } else {
                                    // 当前二级目录全部读取完，进入下一个二级目录
                                    mCurrentSecondaryIndex++;
                                    if (mCurrentSecondaryIndex < mSelectedSecondaries.size()) {
                                        mHandler.sendEmptyMessage(C.LISTEN_GET_SECONDARY);
                                    } else {

                                        if ((Integer) getRightImage().getTag() == 0) {//单词播放，进行暂停
                                            playToggle.setTag(0);
                                            playToggle.setChecked(false);
                                        } else if ((Integer) getRightImage().getTag() == 1) {//循环播放，继续重头开始播放
                                            // 循环播放
                                            mCurrentPage = 0;
                                            mCurrentSecondaryIndex = 0;
                                            mHandler.sendEmptyMessage(C.LISTEN_GET_SECONDARY);
                                        }

                                        //                                }
                                    }
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            Logger.d(throwable.getStackTrace());
                        }
                    });

                    break;
                case C.LISTEN_PLAY_SELECTED_SOUND:
                    // 全文听播放声音
                    if ((Integer) playToggle.getTag() != 1)
                        break;


                    // /storage/emulated/0/kingReading/Res/GZ_4A/page002/sound/p002001.mp3
                    String sdUrl = mLines.get(mCurrentSoundIndex).encryptSoundURL;
                    sdUrl = "http://192.168.3.197:8080/P031006.mp3";
                    MediaPlayerUtil.playFromIntenet(SysApplication.getApplication(), sdUrl, 1.0f, null);
                    MediaPlayerUtil.getInstance().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (mCurrentSoundIndex < mLines.size() - 1) {
                                // 下一句声音
                                mCurrentSoundIndex++;
                                //                                Toast.makeText(SysApplication.getApplication(), mSelectedSecondaries.size() + "个选中，正在播放第" + mCurrentSecondaryIndex + "个孩子，该页共有" + mLines.size() + "个音频，正在播放第" + mCurrentSoundIndex + "个视频", Toast.LENGTH_SHORT).show();
                                mHandler.sendEmptyMessageDelayed(C.LISTEN_PLAY_SELECTED_SOUND, 1000);
                            } else {
                                // 完成当页播放
                                mCurrentPage++;
                                if (mCurrentPage <= mCurrentSecondary.getEndPage()) {
                                    // 二级目录中下一页
                                    Toast.makeText(SysApplication.getApplication(), "播放下一页", Toast.LENGTH_SHORT).show();
                                    mHandler.sendEmptyMessage(C.LISTEN_GET_READING_CONFIG);
                                } else {
                                    nextPart();
                                }
                            }

                        }
                    });
                    break;
                case C.LISTEN_STOP_PLAY_SOUND:
                    // 中断全文听
                    mCurrentPage = 0;
                    mCurrentSecondaryIndex = 0;
                    mCurrenListenTimerCount = 0;
                    playToggle.setTag(0);
                    playToggle.setChecked(false);
                    MediaPlayerUtil.stop();
                    setSecondaryPlaying(-1, true);
                    break;
                case C.LISTEN_GET_SELECTED:
                    getSelectedSecondaries();
                    break;
                case C.LISTEN_TIMER_SENCOND:
                    if (mCurrenListenTimerCount < MLISTENTIMERTYPEONE) {
                        mCurrenListenTimerCount++;
                        Toast.makeText(SysApplication.getApplication(), "定时" + mCurrenListenTimerCount, Toast.LENGTH_SHORT).show();
                    } else {
                        mCurrenListenTimerCount = 0;
                        mTimer.cancel();
                        //并暂停播放
                        playToggle.setChecked(false);
                        playToggle.setTag(2);
                        MediaPlayerUtil.pause();
                        Toast.makeText(SysApplication.getApplication(), "暂停", Toast.LENGTH_SHORT).show();

                        setSecondaryPlaying(mCurrentSecondaryIndex, true);
                    }
                    break;
            }

        }
    };


    /**
     * 播放下一个unit或是part
     */
    private void nextPart() {
        playToggle.setTag(1);// 设置播放状态
        playToggle.setChecked(true);
        // 当前二级目录全部读取完，进入下一个二级目录
        mCurrentSecondaryIndex++;
        if (mCurrentSecondaryIndex < mSelectedSecondaries.size()) {
            mHandler.sendEmptyMessage(C.LISTEN_GET_SECONDARY);
            Toast.makeText(SysApplication.getApplication(), "播放下一个二级目录", Toast.LENGTH_SHORT).show();
            setSecondaryPlaying(mCurrentSecondaryIndex, false);
        } else {


            mCurrentPage = 0;
            mCurrentSecondaryIndex = 0;
            if ((Integer) getRightImage().getTag() == 0) {
                playToggle.setTag(0);
                playToggle.setChecked(false);
                setSecondaryPlaying(-1, false);

            } else if ((Integer) getRightImage().getTag() == 1) {
                // 循环播放
                mHandler.sendEmptyMessage(C.LISTEN_GET_SECONDARY);
                setSecondaryPlaying(mCurrentSecondaryIndex, false);
            }
        }
    }

    /**
     * 显示哪个二级目录正在播放的图标
     *
     * @param currentSecondaryIndex
     */
    private void setSecondaryPlaying(int currentSecondaryIndex, boolean isPause) {
        String title = "";

        for (int i = 0; i < mGroupModules.size(); i++) {
            for (int j = 0; j < mGroupModules.get(i).items.size(); j++) {
                if (currentSecondaryIndex != -1) {
                    title = mSelectedSecondaries.get(currentSecondaryIndex).getTitle();

                }

                if (title.equals(mGroupModules.get(i).items.get(j).getTitle())) {
                    mGroupModules.get(i).items.get(j).setPlaying(true);
                    mGroupModules.get(i).items.get(j).setPause(isPause);
                } else {
                    mGroupModules.get(i).items.get(j).setPlaying(false);
                    mGroupModules.get(i).items.get(j).setPause(false);
                }
            }
        }


        getRecyclerAdapter().notifyDataSetChanged();
    }

    /**
     * 获取所有被选中的二级标题
     */
    private void getSelectedSecondaries() {
        mSelectedSecondaries.clear();
        for (int i = 0; i < mGroupModules.size(); i++) {
            if (mGroupModules.get(i).isChecked) {
                // 整个一级标题被选中则将所包含的二级标题全部添加
                mSelectedSecondaries.addAll(mGroupModules.get(i).items);
            } else {
                for (int j = 0; j < mGroupModules.get(i).items.size(); j++) {
                    if (mGroupModules.get(i).items.get(j).isChecked())
                        mSelectedSecondaries.add(mGroupModules.get(i).items.get(j));
                }
            }
        }
    }

    @Override
    public void onItemChildClick(final BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.chk_listen_textbook_selected:
                ListenModule module = (ListenModule) adapter.getItem(position);
                module.setChecked(!module.isChecked());
                for (ListenUnit unit : module.getItems()) {
                    unit.setChecked(module.isChecked());
                }
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        mHandler.sendEmptyMessage(C.LISTEN_STOP_PLAY_SOUND);
                        mHandler.sendEmptyMessage(C.LISTEN_GET_SELECTED);
                    }
                });
                break;
        }
    }

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_OFFHOOK:// 拨号
                case TelephonyManager.CALL_STATE_RINGING:// 电话正在响铃
                    MediaPlayerUtil.getInstance().pause();
                    playToggle.setTag(0);
                    playToggle.setChecked(false);
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {
                if (bookId == 0 && EmptyUtils.isNotEmpty(getUserRepository().getUser())){
                    bookId = getUserRepository().getUser().usingBook;
                }

                getResRepository().getBookDetail(bookId).subscribe(new Consumer<BookEntity>() {
                    @Override
                    public void accept(@NonNull BookEntity bookEntity) throws Exception {
                        mGroupModules = ViewMappers.mapperListenModule(bookEntity);
                        refreshUI(mGroupModules);
                    }
                });
            }

            @Override
            public void onLoadMore(IBaseRecyclerView netView) {

            }
        };
    }

    public void setPopTimer() {
        if (mPopWindowListemTimer == null) {
            mPopWindowListemTimer = new PopWindowListemTimer(this);

        }
        if (mPopWindowListemTimer.isShowing()) {
            mPopWindowListemTimer.dismiss();
        } else {
            showListenTimerPop();
        }

    }

    /**
     * 显示定时设置的popWindow
     */
    private void showListenTimerPop() {
        mPopWindowListemTimer.showAsDropDown(getRightAssistImage());
        mGdListemTimer = (GridView) mPopWindowListemTimer.getPopWindowView().findViewById(R.id.gd_listen_timer);

        mListenTimerAdapter = new ListenTimerAdapter(mListenTimerTag == 0 ? 6 : mListenTimerTag - 1);
        mGdListemTimer.setAdapter(mListenTimerAdapter);
        mGdListemTimer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SysApplication.getApplication(), "点击了" + position, Toast.LENGTH_SHORT).show();
                mListenTimerAdapter.setCheckedPostion(position);
                mListenTimerAdapter.notifyDataSetChanged();

                if (position <= 5) {
                    mListenTimerTag = position + 1;
                } else if (position == 6) {
                    mListenTimerTag = 0;
                }

                if ((Integer) playToggle.getTag() == 1) {

                    mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(C.LISTEN_TIMER_SENCOND);
                        }
                    }, 0, 1000);

                } else {

                }

            }
        });
    }

    private void startListenTimer() {

    }

    public void cancleListenTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }


}

