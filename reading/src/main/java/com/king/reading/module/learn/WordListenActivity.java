package com.king.reading.module.learn;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.base.activity.RecyclerViewActivity;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.model.WordSpellModule;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by AllynYonge on 14/06/2017.
 */
@Route(path = C.ROUTER_LEARN_WORDLISTEN)
public class WordListenActivity extends RecyclerViewActivity<WordSpellModule>{

    @Override
    public void onInitData(Bundle savedInstanceState) {
    }

    @Override
    public void onInitView() {
        super.onInitView();
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("单词听写");
    }

    @Override
    public BaseQuickAdapter<WordSpellModule, BaseViewHolder> getAdapter() {
        return new BaseQuickAdapter<WordSpellModule, BaseViewHolder>(R.layout.item_listen_word) {
            @Override
            protected void convert(BaseViewHolder helper, WordSpellModule item) {
                helper.setText(R.id.tv_listen_word_unitName, item.unitName);
                helper.addOnClickListener(R.id.constraint_listen_word);
            }
        };
    }

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {
                getResRepository().getWordUnit().subscribe(new Consumer<List<WordSpellModule>>() {
                    @Override
                    public void accept(@NonNull List<WordSpellModule> wordSpellModules) throws Exception {
                        refreshUI(wordSpellModules);
                    }
                });
            }

            @Override
            public void onLoadMore(IBaseRecyclerView netView) {

            }

        };
    }

    @Override
    public int getContentView() {
        return R.layout.activity_wordlisten;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        WordSpellModule module = (WordSpellModule) adapter.getItem(position);
        switch (view.getId()){
            case R.id.constraint_listen_word:
                getNavigation().routerWordListenDetailAct(module.unitId);
                break;
        }

    }
}
