package com.king.reading.module.learn.roleplay;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.base.activity.RecyclerViewActivity;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseMultiItemQuickAdapter;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.common.utils.DialogUtils;
import com.king.reading.common.utils.VoiceEvaluate;
import com.king.reading.ddb.Dialogue;
import com.king.reading.ddb.PlayBook;
import com.king.reading.model.EventMessagePlayBook;
import com.king.reading.model.EventMessageRoleScore;
import com.king.reading.model.RoleElement;
import com.king.reading.model.RoleModel;
import com.king.reading.model.RolePlayItem;
import com.king.reading.widget.DonutProgress;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.unisound.edu.oraleval.sdk.sep15.SDKError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayerUtil;

/**
 * Created by hu.yang on 2017/6/19.
 */

public class RolePlayingDetailActivity extends RecyclerViewActivity<RolePlayItem> implements RoleActManager.RoleActObserver {
    @BindView(R.id.ll_base_content)
    View baseRootView;
    @BindView(R.id.progress_wheel)
    DonutProgress wheel;
    @BindView(R.id.image_roleplay_vioce)
    ImageView mImageRoleplayVioce;
    private List<RolePlayItem> rolePlayItems = new ArrayList<>();
    private List<RoleElement> mRoleElements = new ArrayList<>();//记录得分

    private int mLinesIndex = 0;//正在朗读或角色扮演的序号
    private int mCurrentProgress = 0;
    private int mProgressTotal = 5;
    private boolean mShouldScroll;
    private int mToPosition;
    private PlayBook mPlayBook;
    private List<RoleModel> mSelectedRoles;
    private List<Dialogue> mLines;
    private Timer mTimer;
    private RoleModel mRole;//记录当前句播放的角色是否是选中的状态

    private final Handler handler = new Handler() {
        int i = 0;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*if (msg.what <= 1000) {
                handler.sendEmptyMessageDelayed(i++, 100);
                wheel.setProgress(Float.parseFloat(i + "") / 10f);
            }*/
            switch (msg.what) {
                case C.ROLEPLAY_MIME_RECORD:
                    if (mCurrentProgress <= mProgressTotal) {
                        wheel.setProgress(mProgressTotal - mCurrentProgress);
                        mCurrentProgress++;
                    } else {

                        mCurrentProgress = 0;
                        mTimer.cancel();
                        mLinesIndex++;
                        if (mLinesIndex > mLines.size() - 1) {
                            Toast.makeText(SysApplication.getApplication(), "角色扮演完成", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        playBookActing();
                    }
                    break;
                case C.ROLE_ACT_EXIT:
                    startActivity(new Intent(RolePlayingDetailActivity.this, RolePlayingChoiceActivity.class));
                    finish();
                    break;
                case C.ROLE_ACT_CONTINUE:
                    getLeftImage().setEnabled(true);
                    playBookActing();
                    break;
                case C.ROLE_ACT_RESTART://弹出角色选择对话框
                    DialogUtils.chooseRoleDialog(RolePlayingDetailActivity.this, mPlayBook, mSelectedRoles, handler);
                    break;
                case C.ROLEPLAY_SELECTEDROLE:
                    EventMessagePlayBook eventMessagePlayBook = new EventMessagePlayBook();
                    eventMessagePlayBook = (EventMessagePlayBook) msg.obj;
                    mSelectedRoles = eventMessagePlayBook.getSelectedRoles();
                    mPlayBook = eventMessagePlayBook.getPlayBook();
                    getLeftImage().setEnabled(true);
                    mCurrentProgress = 0;
                    mLinesIndex = 0;
                    initRolePlayItemView();
                    getAdapter().notifyDataSetChanged();
                    playBookActing();
                    break;
                case C.ROLE_ACT_FINISH:
                    for (int j = 0; j < mSelectedRoles.size(); j++) {//重置
                        mSelectedRoles.set(j,new RoleModel(mSelectedRoles.get(j).name,false));
                    }
                    EventMessageRoleScore eventMessageRoleScore = new EventMessageRoleScore();
                    eventMessageRoleScore.setScore(getResultScore());
                    eventMessageRoleScore.setPlayBook(mPlayBook);
                    eventMessageRoleScore.setSelectedRoles(mSelectedRoles);
                    EventBus.getDefault().postSticky(eventMessageRoleScore);
                    startActivity(new Intent(RolePlayingDetailActivity.this,RolePlayScoreActivity.class));
                    RoleActManager.getInstance(SysApplication.getApplication()).unRegisterObserver(RolePlayingDetailActivity.this);
                    finish();
                    break;
            }
        }
    };
    private RxPermissions mRxPermissions;


    @Override
    public void onInitData(Bundle savedInstanceState) {
        /*rolePlayItems.add(new RolePlayItem(RolePlayItem.OTHER,"aaaa","aaaaaaaaaaa"));
        rolePlayItems.add(new RolePlayItem(RolePlayItem.MINE,"aaaa","bbbbbbbbbbbb"));
        rolePlayItems.add(new RolePlayItem(RolePlayItem.OTHER,"aaaa","aaaaaaaaaaa"));*/
        mLinesIndex = 0;
        RoleActManager.getInstance(this).registerObserver(this);
        mRxPermissions = new RxPermissions(this);

        playBookActing();
    }


    @Override
    public void onInitView() {
        super.onInitView();
        baseRootView.setBackgroundResource(R.color.colorPrimaryDark);
        setLeftImageIcon(R.mipmap.ic_stop);
        wheel.setShowText(false);
        //        handler.sendEmptyMessageDelayed(0, 100);

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMessagePlayBook eventMessagePlayBook) {
        mPlayBook = eventMessagePlayBook.getPlayBook();
        mSelectedRoles = eventMessagePlayBook.getSelectedRoles();

        initRolePlayItemView();

    }

    /**
     * 初始item的条目
     */
    private void initRolePlayItemView() {
        rolePlayItems.clear();
        mLines = mPlayBook.getLines();
        for (Dialogue dialogue : mLines) {

            String talker = dialogue.talker;
            for (RoleModel role : mSelectedRoles) {
                if (role.getName().equals(talker)) {
                    if (role.isSelect) {//dialogue该句话，是选中的角色
                        rolePlayItems.add(new RolePlayItem(RolePlayItem.MINE, dialogue.encryptSoundURL, dialogue.content));
                    } else {
                        rolePlayItems.add(new RolePlayItem(RolePlayItem.OTHER, dialogue.encryptSoundURL, dialogue.content));
                    }
                }
            }
        }
    }

    @Override
    public void onLeftClick(View v) {
        //        Toast.makeText(SysApplication.getApplication(), "开始播放", Toast.LENGTH_SHORT).show();
        getLeftImage().setEnabled(false);
        if (mRole.isSelect) {//当前句在录音，暂停录音
            /*mCurrentProgress = 0;
            mTimer.cancel();
            wheel.setVisibility(View.INVISIBLE);
            mImageRoleplayVioce.setVisibility(View.INVISIBLE);*/
            if (RoleActManager.getInstance(this).getEvaluate().getIOralEvalSDK() != null) {
                RoleActManager.getInstance(this).stop(true);
            }else{
                DialogUtils.createRoleActStopDialog(this,handler);
                getLeftImage().setEnabled(true);
            }
            //弹出暂停对话框
            //            DialogUtils.createRoleActStopDialog(this,handler);
        } else {//当前句在播放，暂停播放
            MediaPlayerUtil.getInstance().setOnCompletionListener(null);
            MediaPlayerUtil.getInstance().pause();
            //弹出暂停对话框
            DialogUtils.createRoleActStopDialog(this, handler);
        }

    }

    /**
     * 我和其他角色之间的交互逻辑
     */
    private void playBookActing() {
        Dialogue dialogue = mLines.get(mLinesIndex);
        String talker = dialogue.talker;
        for (RoleModel role : mSelectedRoles) {

            if (role.getName().equals(talker)) {
                mRole = role;
                smoothMoveToPosition(mLinesIndex);

                if (role.isSelect) {//dialogue该句话，是选中的角色


                    wheel.setVisibility(View.VISIBLE);
                    mImageRoleplayVioce.setVisibility(View.VISIBLE);

                    /*wheel.setMax(mProgressTotal);

                    mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(C.ROLEPLAY_MIME_RECORD);
                        }
                    }, 0, 1000);*/

                    final RoleElement element = new RoleElement(dialogue.content, dialogue.encryptSoundURL);
                    mRoleElements.add(element);//最后记录总的得分

                    mRxPermissions.request(Manifest.permission.RECORD_AUDIO)//录音权限处理
                            .subscribe(new Observer<Boolean>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                }

                                @Override
                                public void onNext(@NonNull Boolean aBoolean) {
                                    if (aBoolean){
                                        RoleActManager.getInstance(RolePlayingDetailActivity.this).acting(element);
                                    }else{
                                        Toast.makeText(SysApplication.getApplication(),"权限阻止",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });


                } else {//dialogue该句话，朗读角色

                    wheel.setVisibility(View.INVISIBLE);
                    mImageRoleplayVioce.setVisibility(View.INVISIBLE);

                    MediaPlayerUtil.playFromIntenet(SysApplication.getApplication(), dialogue.encryptSoundURL, null);
                    MediaPlayerUtil.getInstance().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if (mLinesIndex >= mLines.size() - 1) {
                                Toast.makeText(SysApplication.getApplication(), "角色扮演完成", Toast.LENGTH_SHORT).show();
                                handler.sendEmptyMessage(C.ROLE_ACT_FINISH);
                                return;
                            } else {
                                mLinesIndex++;
                                playBookActing();
                            }
                        }
                    });
                }
            }
        }


    }

    /**
     * 需要重写这个方法，不然在可视范围内不会滑倒顶部
     *
     * @param position
     */
    private void smoothMoveToPosition(final int position) {
        int firstItem = getRecyclerView().getChildLayoutPosition(getRecyclerView().getChildAt(0));
        int lastItem = getRecyclerView().getChildLayoutPosition(getRecyclerView().getChildAt(getRecyclerView().getChildCount() - 1));
        if (position < firstItem ) {
            // 如果要跳转的位置在第一个可见项之前，则smoothScrollToPosition可以直接跳转
            getRecyclerView().smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 如果要跳转的位置在第一个可见项之后，且在最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < getRecyclerView().getChildCount()) {
                int top = getRecyclerView().getChildAt(movePosition).getTop();
                getRecyclerView().smoothScrollBy(0, top);
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，进入上一个控制语句
            getRecyclerView().smoothScrollToPosition(position);
            mShouldScroll = true;
            mToPosition = position;
        }
    }


    @Override
    public BaseQuickAdapter<RolePlayItem, BaseViewHolder> getAdapter() {
        return new RolePlayMultiAdapter(rolePlayItems);
    }

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore(IBaseRecyclerView netView) {

            }
        };
    }

    @Override
    public int getContentView() {
        return R.layout.activity_roleplay_detail;
    }

    @Override
    public boolean hasEventBus() {
        return true;
    }


    @Override
    public void onStateChanged(final RoleElement element) {
        wheel.post(new Runnable() {
            @Override
            public void run() {
                setRecordProgress(element);
            }
        });
    }

    @Override
    public void onProgressed(final RoleElement element) {
        wheel.post(new Runnable() {
            @Override
            public void run() {
                setRecordProgress(element);
            }
        });

    }

    @Override
    public void showStopDialog() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onLeftClick(getLeftImage());
            }
        });
    }

    @Override
    public void startRoleAct() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLinesIndex++;
                if (mLinesIndex > mLines.size() - 1) {
                    handler.sendEmptyMessage(C.ROLE_ACT_FINISH);
                    Toast.makeText(SysApplication.getApplication(), "角色扮演完成", Toast.LENGTH_SHORT).show();
                    return;
                }
                playBookActing();
            }
        });
    }

    @Override
    public void showErrorMsg(final SDKError err) {
        wheel.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                VoiceEvaluate.onlineSDKError(RolePlayingDetailActivity.this, err);

            }
        });
    }

    @Override
    public void setStopButtonEnable(final boolean enabled) {
        getLeftImage().post(new Runnable() {
            @Override
            public void run() {
                getLeftImage().setEnabled(enabled);
            }
        });
    }

    private void setRecordProgress(RoleElement element) {
        if (wheel.getMax() != element.getMaxProgress()) {
            wheel.setMax(element.getMaxProgress());
        }

        if (element.getState() == RoleActManager.STATE_NONE) {
            //            wheel.setBackgroundResource(R.drawable.btn_roleact_record_normal);
            wheel.setVisibility(View.INVISIBLE);
            mImageRoleplayVioce.setVisibility(View.INVISIBLE);
        } else if (element.getState() == RoleActManager.STATE_LOADING) {
            wheel.setVisibility(View.VISIBLE);
            mImageRoleplayVioce.setVisibility(View.VISIBLE);

        }
        //            wheel.setBackgroundResource(R.drawable.btn_roleact_record_playing);
        wheel.setProgress(element.getCurProgress());
    }

    /**
     * 测评得分
     * @return
     */
    private float getResultScore() {
        float score = 0.0f;
        for (RoleElement roleElement  : mRoleElements) {
        	score += roleElement.getScore();
        }

        return score/mRoleElements.size();
    }


    private class RolePlayMultiAdapter extends BaseMultiItemQuickAdapter<RolePlayItem, BaseViewHolder> {

        /**
         * Same as QuickAdapter#QuickAdapter(Context,int) but with
         * some initialization data.
         *
         * @param data A new list is created out of this one to avoid mutable list
         */
        public RolePlayMultiAdapter(List<RolePlayItem> data) {
            super(data);
            addItemType(RolePlayItem.MINE, R.layout.item_roleplaying_right);
            addItemType(RolePlayItem.OTHER, R.layout.item_roleplaying_left);
        }

        @Override
        protected void convert(BaseViewHolder helper, RolePlayItem item) {
            helper.setText(R.id.tv_rolePlay_content, item.content);
        }
    }
}
