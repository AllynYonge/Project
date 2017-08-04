package com.king.reading.model;

import java.util.List;

/**
 * 创建者     王开冰
 * 创建时间   2017/8/1 17:53
 * 描述	      跟读闯关得分信息
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class EventMessageBreakThroughResult {
    private String mTitle;
    private List<Follow> mFollows;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public List<Follow> getFollows() {
        return mFollows;
    }

    public void setFollows(List<Follow> follows) {
        mFollows = follows;
    }

    @Override
    public String toString() {
        return "EventMessageBreakThroughResult{" +
                "mTitle='" + mTitle + '\'' +
                ", mFollows=" + mFollows +
                '}';
    }
}
