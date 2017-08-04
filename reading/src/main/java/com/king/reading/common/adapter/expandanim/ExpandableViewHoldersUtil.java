package com.king.reading.common.adapter.expandanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.model.ExpandNestedList;

public class ExpandableViewHoldersUtil {

    public static void openH(final RecyclerView.ViewHolder holder, final View expandView, final boolean animate) {
        if (animate) {
            expandView.setVisibility(View.VISIBLE);
            final Animator animator = ViewHolderAnimator.ofItemViewHeight(holder);
            /*animator.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animation) {
                    final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(expandView, View.ALPHA, 1);
                    alphaAnimator.addListener(new ViewHolderAnimator.ViewHolderAnimatorListener(holder));
                    alphaAnimator.start();
                }
            });*/
            animator.start();
        }
        else {
            expandView.setVisibility(View.VISIBLE);
            //expandView.setAlpha(1);
        }
    }

    public static void closeH(final RecyclerView.ViewHolder holder, final View expandView, final boolean animate) {
        if (animate) {
            expandView.setVisibility(View.GONE);
            final Animator animator = ViewHolderAnimator.ofItemViewHeight(holder);
            expandView.setVisibility(View.VISIBLE);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animation) {
                    expandView.setVisibility(View.GONE);
                    //expandView.setAlpha(0);
                }
                @Override public void onAnimationCancel(Animator animation) {
                    expandView.setVisibility(View.GONE);
                    //expandView.setAlpha(0);
                }
            });
            animator.start();
        }
        else {
            expandView.setVisibility(View.GONE);
            //expandView.setAlpha(0);
        }
    }

    public interface Expandable {
        View getExpandView();
    }

    public static class KeepOneH<VH extends BaseViewHolder/* & Expandable*/> {

        public void toggle(VH holder, View expandView, ExpandNestedList item) {
            if (item.isExpand()){
                ExpandableViewHoldersUtil.closeH(holder, expandView, true);
            } else {
                ExpandableViewHoldersUtil.openH(holder, expandView, true);
            }
            item.setExpand(!item.isExpand());
        }
    }

}
