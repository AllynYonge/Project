package com.king.reading.module.learn.roleplay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.common.utils.DialogUtils;
import com.king.reading.model.EventMessagePlayBook;
import com.king.reading.model.EventMessageRoleScore;
import com.king.reading.ddb.PlayBook;
import com.king.reading.model.RoleModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hu.yang on 2017/6/19.
 */

public class RolePlayScoreActivity extends BaseActivity {
    @BindView(R.id.image_rolePlay_expression)
    ImageView mImageRolePlayExpression;
    @BindView(R.id.image_rolePlay_score_text)
    ImageView mImageRolePlayScoreText;
    @BindView(R.id.tv_role_continue)
    TextView mTvRoleContinue;
    private PlayBook mPlayBook;
    private List<RoleModel> mSelectedRoles;

    @Override
    public void onInitData(Bundle savedInstanceState) {

    }


    @Override
    public void onInitView() {
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("角色扮演得分");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_roleplay_score;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMessageRoleScore eventMessageRoleScore) {
        float score = eventMessageRoleScore.getScore();
        mPlayBook = eventMessageRoleScore.getPlayBook();
        mSelectedRoles = eventMessageRoleScore.getSelectedRoles();
        Toast.makeText(SysApplication.getApplication(), "总得分：" + score, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLeftClick(View v) {
        startActivity(new Intent(this, RolePlayingChoiceActivity.class));
        finish();
    }

    @OnClick(R.id.tv_role_continue)
    public void continueRoleAct(View v){
        DialogUtils.chooseRoleDialog(this,mPlayBook,mSelectedRoles,mHandler);
    }

    android.os.Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case C.ROLEPLAY_SELECTEDROLE:
                    EventMessagePlayBook eventMessagePlayBook = (EventMessagePlayBook) msg.obj;
                    EventBus.getDefault().postSticky(eventMessagePlayBook);
                    startActivity(new Intent(RolePlayScoreActivity.this,RolePlayingDetailActivity.class));
                    finish();
                    break;
            }
        }
    };

    @Override
    public boolean hasEventBus() {
        return true;
    }


}
