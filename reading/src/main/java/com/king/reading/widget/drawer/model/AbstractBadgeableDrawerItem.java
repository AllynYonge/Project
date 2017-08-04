package com.king.reading.widget.drawer.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;

import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.widget.badgeview.QBadgeView;
import com.king.reading.widget.drawer.holder.BadgeStyle;
import com.king.reading.widget.drawer.holder.StringHolder;
import com.king.reading.widget.drawer.model.interfaces.ColorfulBadgeable;

import java.util.List;

/**
 * Created by mikepenz on 03.02.15.
 */
public abstract class AbstractBadgeableDrawerItem<Item extends AbstractBadgeableDrawerItem> extends BaseDescribeableDrawerItem<Item, BaseViewHolder> implements ColorfulBadgeable<Item> {
    protected StringHolder mBadge;
    protected BadgeStyle mBadgeStyle = new BadgeStyle();

    @Override
    public Item withBadge(StringHolder badge) {
        this.mBadge = badge;
        return (Item) this;
    }

    @Override
    public Item withBadge(String badge) {
        this.mBadge = new StringHolder(badge);
        return (Item) this;
    }

    @Override
    public Item withBadge(@StringRes int badgeRes) {
        this.mBadge = new StringHolder(badgeRes);
        return (Item) this;
    }

    @Override
    public Item withBadgeStyle(BadgeStyle badgeStyle) {
        this.mBadgeStyle = badgeStyle;
        return (Item) this;
    }

    public StringHolder getBadge() {
        return mBadge;
    }

    public BadgeStyle getBadgeStyle() {
        return mBadgeStyle;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_primary;/*"PRIMARY_ITEM"*/
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_primary;
    }

    @Override
    public void bindView(BaseViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);

        /*Context ctx = viewHolder.itemView.getContext();
        //bind the basic view parts
        bindViewHelper(viewHolder);

        //set the text for the badge or hide
        boolean badgeVisible = StringHolder.applyToOrHide(mBadge, viewHolder.badge);
        //style the badge if it is visible
        if (badgeVisible) {
            mBadgeStyle.style(viewHolder.badge, getTextColorStateList(getColor(ctx), getSelectedTextColor(ctx)));
            viewHolder.badgeContainer.setVisibility(View.VISIBLE);
        } else {
            viewHolder.badgeContainer.setVisibility(View.GONE);
        }

        //define the typeface for our textViews
        if (getTypeface() != null) {
            viewHolder.badge.setTypeface(getTypeface());
        }

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);*/

        Context ctx = viewHolder.itemView.getContext();
        //bind the basic view parts
        bindViewHelper(viewHolder);

        //style the badge if it is visible
        if (mBadge != null) {
            TextPaint textPaint = viewHolder.name.getPaint();
            float textPaintWidth = textPaint.measureText(viewHolder.name.getText().toString());
            QBadgeView badgeView = new QBadgeView(SysApplication.getApplication());
            badgeView.bindTarget(viewHolder.name)
                    .setBadgePadding(4,true)
                    .setBadgeGravity(Gravity.END | Gravity.TOP)
                    .setBadgeText(mBadge.getText().toString())
                    .setBadgeTextColor(ctx.getResources().getColor(R.color.red))
                    .setBadgeBackgroundColor(ctx.getResources().getColor(R.color.red));
                    /*.setBadgeNumber(1)
                    .setBadgeBackgroundColor(ctx.getResources().getColor(R.color.red));*/
        }

        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public BaseViewHolder getViewHolder(View v) {
        return new BaseViewHolder(v);
    }

}
