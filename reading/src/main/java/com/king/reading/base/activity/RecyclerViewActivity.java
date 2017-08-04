package com.king.reading.base.activity;

import android.graphics.Color;
import android.support.annotation.CallSuper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.king.reading.R;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.util.SpaceItemDecoration;
import com.king.reading.common.utils.Check;
import com.king.reading.widget.drawer.util.DensityUtils;
import com.king.reading.widget.statelayout.StatefulLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by hu.yang on 2017/6/7.
 */

public abstract class RecyclerViewActivity<T> extends BaseActivity implements IBaseRecyclerView<T>,BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener{
    private final static int PRELOADNUMBER = 10;
    @BindView(R.id.recycle) RecyclerView mRecyclerView;
    @BindView(R.id.swipeLayout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.stateLayout) StatefulLayout mStateLayout;
    private BaseQuickAdapter mAdapter;
    private INetPresenter presenter;
    /**
     * 是否自动刷新
     */
    private boolean autoRefresh = true;

    /**
     * 是否允许下拉刷新
     */
    private boolean enableRefresh = true;

    @CallSuper
    @Override
    public void onInitView() {
        mAdapter = getAdapter();
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mAdapter.setPreLoadNumber(PRELOADNUMBER);
        presenter = getPresenter();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(getVerticalInterval()));
        mRecyclerView.setAdapter(mAdapter);
        recyclerViewConfig(mRecyclerView,mAdapter);

        if (autoRefresh)
            onRefresh();
        mSwipeRefreshLayout.setEnabled(enableRefresh);
    }

    @Override
    public int getContentView() {
        return R.layout.layout_recycler;
    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
        if (presenter != null){
            showLoading();
            presenter.onRefresh();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeRefreshLayout.setEnabled(false);
        if (presenter != null)
            presenter.onLoadMore(this);
    }

    @Override
    public void refreshUI(List<T> data) {
        if (Check.isNotEmpty(data)){
            mStateLayout.showContent();
            mAdapter.setNewData(data);
            //隐藏refreshLayout
            mAdapter.setEnableLoadMore(true);
            if (data.size() < PRELOADNUMBER){
                mAdapter.loadMoreEnd(true);
            }
        } else {
            mStateLayout.showEmpty();
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMore(List<T> data, boolean hasMore) {
        if (Check.isEmpty(data)){
            mStateLayout.showEmpty();
            return;
        }

        mAdapter.addData(data);
        mStateLayout.showContent();
        if (!hasMore){
            mAdapter.loadMoreEnd(true);
        }
        mAdapter.loadMoreComplete();
        mSwipeRefreshLayout.setEnabled(true);
    }

    public void setEnableRefresh(boolean isEnable){
        this.enableRefresh = isEnable;
    }

    public void setAutoRefresh(boolean autoRefresh){
        this.autoRefresh = autoRefresh;
    }

    protected void recyclerViewConfig(RecyclerView recyclerView, BaseQuickAdapter adapter){}

    public int getVerticalInterval(){
        return DensityUtils.dp2px(getApplicationContext(), 10);
    }

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    public BaseQuickAdapter getRecyclerAdapter(){
        return mAdapter;
    }

    protected RecyclerView.LayoutManager getLayoutManager(){
        return new LinearLayoutManager(this);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {}
}
