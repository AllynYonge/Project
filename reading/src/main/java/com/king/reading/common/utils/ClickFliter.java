package com.king.reading.common.utils;

/**
 * 防止重复点击
 */
public class ClickFliter {

    public static final int MIN_CLICK_DELAY_TIME = 500;
    private static long[] violencecClick = new long[2];

    public static boolean canClick() {
        violencecClick[1] = System.currentTimeMillis();
        if (Math.abs(violencecClick[1] - violencecClick[0]) >= MIN_CLICK_DELAY_TIME) {
            violencecClick[0] = violencecClick[1];
            return true;
        } else {
            return false;
        }
    }
}
