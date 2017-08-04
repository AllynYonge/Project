package com.king.reading.widget.drawer.model;

/**
 * Created by mikepenz on 03.02.15.
 */
public class PrimaryDrawerItem extends AbstractBadgeableDrawerItem<PrimaryDrawerItem> {

    /*@Override
    public void setBadge(BaseViewHolder viewHolder, Context ctx) {
        TextPaint textPaint = viewHolder.name.getPaint();
        float textPaintWidth = textPaint.measureText(viewHolder.name.getText().toString());
        QBadgeView qBadgeView = new QBadgeView(ctx);
        qBadgeView.bindTarget(viewHolder.name)
                .setBadgePadding(4,true)
                .setBadgeGravity(Gravity.START | Gravity.TOP)
                .setGravityOffset(textPaintWidth + DensityUtils.dp2px(ctx,8), DensityUtils.dp2px(ctx,14), false)
                .setBadgeText("")
                .setBadgeBackgroundColor(ctx.getResources().getColor(R.color.red));
    }*/
}
