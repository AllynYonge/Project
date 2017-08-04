package com.king.reading.common.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.flexbox.FlexboxLayout;
import com.king.reading.common.adapter.expandanim.ExpandableViewHoldersUtil;
import com.king.reading.model.ExpandNestedList;

import java.util.List;

/**
 * Created by hu.yang on 2017/6/5.
 */

public abstract class BaseExpandableAnimAdapter<T extends ExpandNestedList> extends BaseLinearGridAdapter<T> {
    final ExpandableViewHoldersUtil.KeepOneH<BaseViewHolder> keepOne = new ExpandableViewHoldersUtil.KeepOneH<BaseViewHolder>();

    public BaseExpandableAnimAdapter(@LayoutRes int layoutId, @Nullable List<T> data, int orientation) {
        super(layoutId, data, orientation);
    }

    public BaseExpandableAnimAdapter(@LayoutRes int layoutId, @Nullable List<T> data, int orientation, int columnCount) {
        super(layoutId, data, orientation, columnCount);
    }


    @Override
    protected void handlerContainerLayout(final BaseViewHolder holder, final T item, final FlexboxLayout containerLayout) {
        if (item.isExpand()) ExpandableViewHoldersUtil.openH(holder, containerLayout, false);
        else ExpandableViewHoldersUtil.closeH(holder, containerLayout, false);
        holder.setOnClickListener(getToggleExpandId(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepOne.toggle(holder, containerLayout, item);
                onExpandChange(holder, item.isExpand());
            }
        });
    }

    protected void onExpandChange(BaseViewHolder helper, boolean isExpand){}

    protected abstract @IdRes int getToggleExpandId();
}