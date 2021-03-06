package com.king.reading.widget.drawer.model;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;


import com.king.reading.R;
import com.king.reading.widget.drawer.Drawer;
import com.king.reading.widget.drawer.holder.ColorHolder;
import com.king.reading.widget.drawer.model.interfaces.IDrawerItem;

import java.util.List;

/**
 * Created by mikepenz on 03.02.15.
 * NOTE: The arrow will just animate (and rotate) on APIs higher than 11 as the ViewCompat will skip this on API 10
 */
public class ExpandableDrawerItem extends BaseDescribeableDrawerItem<ExpandableDrawerItem, ExpandableDrawerItem.ViewHolder> {

    private Drawer.OnDrawerItemClickListener mOnDrawerItemClickListener;

    protected ColorHolder arrowColor;

    protected int arrowRotationAngleStart = 0;

    protected int arrowRotationAngleEnd = 180;

    public ExpandableDrawerItem withArrowColor(@ColorInt int arrowColor) {
        this.arrowColor = ColorHolder.fromColor(arrowColor);
        return this;
    }

    public ExpandableDrawerItem withArrowColorRes(@ColorRes int arrowColorRes) {
        this.arrowColor = ColorHolder.fromColorRes(arrowColorRes);
        return this;
    }

    public ExpandableDrawerItem withArrowRotationAngleStart(int angle) {
        this.arrowRotationAngleStart = angle;
        return this;
    }

    public ExpandableDrawerItem withArrowRotationAngleEnd(int angle) {
        this.arrowRotationAngleEnd = angle;
        return this;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_expandable;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_expandable;
    }

    @Override
    public ExpandableDrawerItem withOnDrawerItemClickListener(Drawer.OnDrawerItemClickListener onDrawerItemClickListener) {
        mOnDrawerItemClickListener = onDrawerItemClickListener;
        return this;
    }

    @Override
    public Drawer.OnDrawerItemClickListener getOnDrawerItemClickListener() {
        return mOnArrowDrawerItemClickListener;
    }

    /**
     * our internal onDrawerItemClickListener which will handle the arrow animation
     */
    private Drawer.OnDrawerItemClickListener mOnArrowDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            if (drawerItem instanceof AbstractDrawerItem && drawerItem.isEnabled()) {
                if (((AbstractDrawerItem) drawerItem).getSubItems() != null) {
                    if (((AbstractDrawerItem) drawerItem).isExpanded()) {
                        ViewCompat.animate(view.findViewById(R.id.material_drawer_arrow)).rotation(ExpandableDrawerItem.this.arrowRotationAngleEnd).start();
                    } else {
                        ViewCompat.animate(view.findViewById(R.id.material_drawer_arrow)).rotation(ExpandableDrawerItem.this.arrowRotationAngleStart).start();
                    }
                }
            }

            return mOnDrawerItemClickListener != null && mOnDrawerItemClickListener.onItemClick(view, position, drawerItem);
        }
    };

    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);

        Context ctx = viewHolder.itemView.getContext();
        //bind the basic view parts
        bindViewHelper(viewHolder);

        //make sure all animations are stopped
        viewHolder.arrow.clearAnimation();
        if (!isExpanded()) {
            ViewCompat.setRotation(viewHolder.arrow, this.arrowRotationAngleStart);
        } else {
            ViewCompat.setRotation(viewHolder.arrow, this.arrowRotationAngleEnd);
        }

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public static class ViewHolder extends BaseViewHolder {
        public ImageView arrow;

        public ViewHolder(View view) {
            super(view);
            arrow = (ImageView) view.findViewById(R.id.material_drawer_arrow);
            arrow.setImageResource(R.drawable.ic_arrow_down);
        }
    }
}
