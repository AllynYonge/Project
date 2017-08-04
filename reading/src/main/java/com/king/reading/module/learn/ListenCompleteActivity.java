package com.king.reading.module.learn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.king.reading.R;
import com.king.reading.base.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by AllynYonge on 23/06/2017.
 */

public class ListenCompleteActivity extends BaseActivity {
    @BindView(R.id.tv_check_word_answer)
    TextView mTvCheckWordAnswer;
    @BindView(R.id.tv_listen_again)
    TextView mTvListenAgain;

    @Override
    public void onInitData(Bundle savedInstanceState) {

    }

    @Override
    public void onInitView() {
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("Unit1");
        setRightText("目录");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_listen_complete;
    }

    @OnClick(R.id.tv_check_word_answer)
    public void checkWordAnwer(View view){
        startActivity(new Intent(ListenCompleteActivity.this,ListenResultActivity.class));
//        finish();
    }

    @OnClick(R.id.tv_listen_again)
    public void listenWordAgain(View view){
        startActivity(new Intent(ListenCompleteActivity.this,WordListenDetailActivity.class));
        finish();
    }
}
