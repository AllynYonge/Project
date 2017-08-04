package com.king.reading.module.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.king.reading.C;
import com.king.reading.Navigation;
import com.king.reading.R;
import com.king.reading.base.activity.RecyclerViewActivity;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.data.entities.BookBaseEntity;
import com.king.reading.data.repository.ResRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by AllynYonge on 11/07/2017.
 */

@Route(path = C.ROUTER_SELECTVERSION)
public class SelectVersionActivity extends RecyclerViewActivity<BookBaseEntity>{
    @Inject ResRepository repository;
    @Inject Navigation navigation;
    @Override
    public void onInitData(Bundle savedInstanceState) {
        getAppComponent().plus().inject(this);
    }

    @Override
    public void onInitView() {
        super.onInitView();
        setCenterTitle("请选择版本");
        setEnableRefresh(false);
    }

    @Override
    public BaseQuickAdapter<BookBaseEntity, BaseViewHolder> getAdapter() {
        return new BaseQuickAdapter<BookBaseEntity, BaseViewHolder>(R.layout.item_selectversion) {
            @Override
            protected void convert(BaseViewHolder helper, BookBaseEntity item) {
                helper.setText(R.id.tv_bookVersion, item.areaName);
                helper.addOnClickListener(R.id.tv_bookVersion);
            }
        };
    }

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {
                repository.getAllVersions().subscribe(new Consumer<List<BookBaseEntity>>() {
                    @Override
                    public void accept(@NonNull List<BookBaseEntity> books) throws Exception {
                        refreshUI(books);
                    }
                });
            }

            @Override
            public void onLoadMore(IBaseRecyclerView netView) {
                getRecyclerAdapter().loadMoreEnd(true);
            }

        };
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        BookBaseEntity item = (BookBaseEntity) adapter.getItem(position);
        switch (view.getId()){
            case R.id.tv_bookVersion:
                navigation.routerSelectBookAct(item.areaName);
                break;
        }

    }
}
