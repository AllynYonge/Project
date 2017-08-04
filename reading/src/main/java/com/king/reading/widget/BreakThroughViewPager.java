package com.king.reading.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;


/**
 * 创建者     王开冰
 * 创建时间   2017/7/31 11:48
 * 描述	      设置是否可向左滑动的ViewPager
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class BreakThroughViewPager extends ViewPager {
    private boolean mIsToLeftScroll = false;//向左滑动
    private float beforeX;

    public BreakThroughViewPager(Context context) {
        super(context);
    }

    public BreakThroughViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIsToLedtScroll(boolean isToLedtScroll){
        mIsToLeftScroll = isToLedtScroll;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://按下如果‘仅’作为‘上次坐标’，不妥，因为可能存在左滑，motionValue大于0的情况（来回滑，只要停止坐标在按下坐标的右边，左滑仍然能滑过去）
                beforeX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float motionValue = ev.getX() - beforeX;
                if (motionValue < 0 && !mIsToLeftScroll) {//禁止左滑
                    return true;
                }

                Log.d("BreakThroughViewPager", "motionValue=" + motionValue);
                beforeX = ev.getX();//手指移动时，再把当前的坐标作为下一次的‘上次坐标’，解决上述问题

                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
