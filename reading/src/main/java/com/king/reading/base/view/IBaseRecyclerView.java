package com.king.reading.base.view;

import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by hu.yang on 2017/6/7.
 */

public interface IBaseRecyclerView<T> {
    void refreshUI(List<T> data);

    void loadMore(List<T> data, boolean hasMore);

    BaseQuickAdapter<T, ? extends BaseViewHolder> getAdapter();

    INetPresenter getPresenter();
}
