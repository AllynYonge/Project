package com.king.reading.module.read;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.king.reading.R;
import com.king.reading.widget.drawer.holder.StringHolder;
import com.king.reading.widget.drawer.model.BaseDrawerItem;

import java.util.List;

/**
 * Created by hu.yang on 2017/6/12.
 */

public class OutlineModuleDrawerItem extends BaseDrawerItem<OutlineModuleDrawerItem, OutlineModuleDrawerItem.ViewHolder>{
    private StringHolder content;
    @Override
    public OutlineModuleDrawerItem withSubItems(List subItems) {
        return null;
    }

    public OutlineModuleDrawerItem withContent(String contentName){
        content = new StringHolder(contentName);
        return this;
    }

    public StringHolder getContent(){
        return content;
    }


    @Override
    public int getType() {
        return ReadDetailActivity.MODULE;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.item_reading_menu_module;
    }

    @Override
    public void bindView(OutlineModuleDrawerItem.ViewHolder holder, List payloads) {
        super.bindView(holder, payloads);
        Context ctx = holder.itemView.getContext();
        holder.itemView.setEnabled(isEnabled());
        StringHolder.applyToOrHide(this.getContent(), holder.content);
        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.setId(hashCode());
        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
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
            this.content = (TextView) view.findViewById(R.id.tv_reading_detail_menu_module);
        }
    }
}
