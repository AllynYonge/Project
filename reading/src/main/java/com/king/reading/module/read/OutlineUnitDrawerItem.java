package com.king.reading.module.read;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.king.reading.R;
import com.king.reading.widget.drawer.holder.StringHolder;
import com.king.reading.widget.drawer.model.BaseDrawerItem;
import com.mikepenz.materialize.util.UIUtils;

import java.util.List;

/**
 * Created by hu.yang on 2017/6/12.
 */

public class OutlineUnitDrawerItem extends BaseDrawerItem<OutlineUnitDrawerItem, OutlineUnitDrawerItem.ViewHolder>{
    private StringHolder content;
    @Override
    public OutlineUnitDrawerItem withSubItems(List subItems) {
        return null;
    }

    public OutlineUnitDrawerItem withContent(String contentName){
        content = new StringHolder(contentName);
        return this;
    }

    public StringHolder getContent(){
        return content;
    }


    @Override
    public int getType() {
        return ReadDetailActivity.UNIT;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.item_reading_menu_unit;
    }

    @Override
    public void bindView(OutlineUnitDrawerItem.ViewHolder holder, List payloads) {
        super.bindView(holder, payloads);
        Context ctx = holder.itemView.getContext();
        holder.itemView.setEnabled(isEnabled());
        holder.itemView.setSelected(isSelected());
        //get the correct color for the background
        int selectedColor = getSelectedColor(ctx);
        //get the correct color for the text
        int color = getColor(ctx);
        ColorStateList selectedTextColor = getTextColorStateList(color, getSelectedTextColor(ctx));

        //set the background for the item
        UIUtils.setBackground(holder.content, UIUtils.getSelectableBackground(ctx, selectedColor, isSelectedBackgroundAnimated()));
        //set the text for the name
        StringHolder.applyTo(this.getName(), holder.content);
        //set the colors for textViews
        holder.content.setTextColor(selectedTextColor);
        onPostBindView(this, holder.itemView);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView content;
        private ViewHolder(View view) {
            super(view);
            this.content = (TextView) view.findViewById(R.id.tv_reading_detail_menu_unit);
        }
    }
}
