package com.king.reading.module.learn;

import android.os.Bundle;

import com.king.reading.R;
import com.king.reading.base.activity.RecyclerViewActivity;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.ddb.Word;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AllynYonge on 22/06/2017.
 */

public class ListenResultActivity extends RecyclerViewActivity<String>{
    private List<String> results = new ArrayList<>();;

    @Override
    public void onInitData(Bundle savedInstanceState) {
        /*results.add("1.aaaaa");
        results.add("2.bbbbb");
        results.add("3.ddddd");
        results.add("4.ccccc");
        results.add("5.fffff");
        results.add("6.ggggg");
        results.add("7.hhhha");*/
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(List<Word> words) {
        for (int i = 0; i < words.size(); i++) {
            results.add((i+1)+"."+words.get(i).getMean());
        }
    }

    @Override
    public void onInitView() {
        super.onInitView();
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("核对答案");
        setEnableRefresh(false);
    }

    @Override
    public BaseQuickAdapter<String, BaseViewHolder> getAdapter() {
        return new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_listen_result, results) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_listen_result, item);
            }
        };
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
    public boolean hasEventBus() {
        return true;
    }
}
