package com.king.reading.common.adapter.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.king.reading.common.adapter.BaseQuickAdapter;

/**
 * Created by hu.yang on 2017/6/5.
 */

public class GridItemColumnDecoration extends RecyclerView.ItemDecoration{
    private int spanCount;
    private int spacing;

    public GridItemColumnDecoration(int spanCount, int spacing) {
        this.spanCount = spanCount;
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //排除header的影响
        BaseQuickAdapter quickAdapter = (BaseQuickAdapter) parent.getAdapter();
        int position = parent.getChildAdapterPosition(view); // item position
        if (position < quickAdapter.getHeaderLayoutCount()){
            return;
        }

        int column = (position - quickAdapter.getHeaderLayoutCount()) % spanCount; // item column


        outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
        outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f / spanCount) * spacing)

        //top需要留间隔
        /*if (position < spanCount) { // top edge
            outRect.top = spacing;
        }*/
        //bottom不需要留间隔
        /*if (position < parent.createAdapter().getItemCount() - spanCount){
            outRect.bottom = spacing; // item bottom
        }*/
    }
}
