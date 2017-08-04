package com.king.reading.widget.drawer.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.reading.R;
import com.king.reading.widget.drawer.Drawer;
import com.king.reading.widget.drawer.holder.BadgeStyle;
import com.king.reading.widget.drawer.holder.ColorHolder;
import com.king.reading.widget.drawer.holder.StringHolder;
import com.king.reading.widget.drawer.model.interfaces.ColorfulBadgeable;
import com.king.reading.widget.drawer.model.interfaces.IDrawerItem;

import java.util.List;

/**
 * Created by mikepenz on 03.02.15.
 * NOTE: The arrow will just animate (and rotate) on APIs higher than 11 as the ViewCompat will skip this on API 10
 */
public class ExpandableBadgeDrawerItem extends BaseDescribeableDrawerItem<ExpandableBadgeDrawerItem, ExpandableBadgeDrawerItem.ViewHolder>
        implements ColorfulBadgeable<ExpandableBadgeDrawerItem> {

    private Drawer.OnDrawerItemClickListener mOnDrawerItemClickListener;

    protected ColorHolder arrowColor;

    protected int arrowRotationAngleStart = 0;

    protected int arrowRotationAngleEnd = 180;

    protected StringHolder mBadge;
    protected BadgeStyle mBadgeStyle = new BadgeStyle();

    @Override
    public int getType() {
        return R.id.material_drawer_item_expandable_badge;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_expandable_badge;
    }

    @Override
    public void bindView(ExpandableBadgeDrawerItem.ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);

        Context ctx = viewHolder.itemView.getContext();
        //bind the basic view parts
        bindViewHelper(viewHolder);

        //set the text for the badge or hide
        boolean badgeVisible = StringHolder.applyToOrHide(mBadge, viewHolder.badge);
        //style the badge if it is visible
        if (true) {
            mBadgeStyle.style(viewHolder.badge, getTextColorStateList(getColor(ctx), getSelectedTextColor(ctx)));
            viewHolder.badgeContainer.setVisibility(View.VISIBLE);
        } else {
            viewHolder.badgeContainer.setVisibility(View.GONE);
        }

        //define the typeface for our textViews
        if (getTypeface() != null) {
            viewHolder.badge.setTypeface(getTypeface());
        }

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
    public ExpandableBadgeDrawerItem withOnDrawerItemClickListener(Drawer.OnDrawerItemClickListener onDrawerItemClickListener) {
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
                        ViewCompat.animate(view.findViewById(R.id.material_drawer_arrow)).rotation(ExpandableBadgeDrawerItem.this.arrowRotationAngleEnd).start();
                    } else {
                        ViewCompat.animate(view.findViewById(R.id.material_drawer_arrow))
                                .rotation(ExpandableBadgeDrawerItem.this.arrowRotationAngleStart)
                                .start();
                    }
                }
            }

            return mOnDrawerItemClickListener != null && mOnDrawerItemClickListener.onItemClick(view, position, drawerItem);
        }
    };

    @Override
    public ExpandableBadgeDrawerItem withBadge(StringHolder badge) {
        this.mBadge = badge;
        return (ExpandableBadgeDrawerItem) this;
    }

    @Override
    public ExpandableBadgeDrawerItem withBadge(String badge) {
        this.mBadge = new StringHolder(badge);
        return (ExpandableBadgeDrawerItem) this;
    }

    @Override
    public ExpandableBadgeDrawerItem withBadge(@StringRes int badgeRes) {
        this.mBadge = new StringHolder(badgeRes);
        return (ExpandableBadgeDrawerItem) this;
    }

    @Override
    public ExpandableBadgeDrawerItem withBadgeStyle(BadgeStyle badgeStyle) {
        this.mBadgeStyle = badgeStyle;
        return (ExpandableBadgeDrawerItem) this;
    }

    public StringHolder getBadge() {
        return mBadge;
    }

    public BadgeStyle getBadgeStyle() {
        return mBadgeStyle;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public static class ViewHolder extends BaseViewHolder {
        public ImageView arrow;
        public View badgeContainer;
        public TextView badge;

        public ViewHolder(View view) {
            super(view);
            badgeContainer = view.findViewById(R.id.material_drawer_badge_container);
            badge = (TextView) view.findViewById(R.id.material_drawer_badge);
            arrow = (ImageView) view.findViewById(R.id.material_drawer_arrow);
            arrow.setImageResource(R.drawable.ic_arrow_down);
        }
    }
}
