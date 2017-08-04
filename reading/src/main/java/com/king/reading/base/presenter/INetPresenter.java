package com.king.reading.base.presenter;

import com.king.reading.base.view.IBaseRecyclerView;

/**
 * Created by hu.yang on 2017/6/7.
 */

public interface INetPresenter<T> {
    void onRefresh();

    void onLoadMore(IBaseRecyclerView<T> netView);
}
