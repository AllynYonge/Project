package com.king.reading.common.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;
import com.king.reading.R;
import com.king.reading.model.BaseNestedList;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by AllynYonge on 15/06/2017.
 */

public abstract class BaseLinearGridAdapter<T extends BaseNestedList> extends BaseQuickAdapter<T, BaseViewHolder> {
    public final static int HORIZONTAL = 1;
    public final static int VERTICAL = 2;
    private final static int DEFAULT_COLUMN_NUM = 3;

    private int mOrientation = HORIZONTAL;
    private int mColumnCount = DEFAULT_COLUMN_NUM;

    public BaseLinearGridAdapter(@LayoutRes int layoutId, @Nullable List<T> data, int orientation) {
        super(layoutId,data);
        mOrientation = orientation;
    }

    public BaseLinearGridAdapter(@LayoutRes int layoutId, @Nullable List<T> data, int orientation, int columnCount) {
        this(layoutId, data, orientation);
        mColumnCount = columnCount;
    }

    @CallSuper
    @Override
    protected void convert(final BaseViewHolder helper, final T item) {

        //获取原有定义的容器
        View containerView = helper.getView(R.id.recycler_expand_placeholder);
        if (containerView == null)
            Logger.e("please set containerId recycler_expand_placeholder");

        //检查复用的holder中是否已经存在FlexboxLayout
        int expandContainerId = mOrientation == HORIZONTAL ? R.id.flexLayout_expandContainer_horizontal : R.id.flexLayout_expandContainer_vertical;
        FlexboxLayout flexboxLayout = helper.getView(expandContainerId);
        if (flexboxLayout == null){
            //创建flexBoxLayout
            if(mOrientation == HORIZONTAL)
                flexboxLayout = createHorizontalLayout(item, mColumnCount);
            else {
                flexboxLayout = createVerticalLayout(item);
            }

            //删除原有的容器，用FlexBoxLayout替换，flexBoxLayout更加方便
            ViewGroup rootView = (ViewGroup)containerView.getParent();
            rootView.removeView(containerView);
            rootView.addView(flexboxLayout,containerView.getLayoutParams());
        }

        int childCount = flexboxLayout.getChildCount();
        int index;
        for (index = 0; index < item.getItems().size(); index++){
            ViewGroup itemView;
            if (index < flexboxLayout.getChildCount()){
                itemView = (ViewGroup) flexboxLayout.getChildAt(index);
            } else {
                itemView = (ViewGroup) getItemView(getChildLayoutRes(),flexboxLayout);
                flexboxLayout.addView(itemView);
            }
            bindChildData(itemView, item, index);
        }

        if (childCount > index){
            for (int i = childCount; i > item.getItems().size(); i--){
                Logger.d("childCount : %d, removeView : %d", childCount, i);
                flexboxLayout.removeViewAt(i - 1);
            }
        }
        handlerContainerLayout(helper, item, flexboxLayout);
    }

    private FlexboxLayout createHorizontalLayout(T item, int columnNum){
        return (FlexboxLayout) getItemView(R.layout.layout_flexlayout_horizontal,null);
    }

    private FlexboxLayout createVerticalLayout(T item){
        return (FlexboxLayout) getItemView(R.layout.layout_flexlayout_vertical,null);
    }

    protected abstract @LayoutRes int getChildLayoutRes();
    protected abstract void bindChildData(ViewGroup childHolder,T group, int index);
    protected void handlerContainerLayout(BaseViewHolder holder, T item, FlexboxLayout containerLayout){}
}
