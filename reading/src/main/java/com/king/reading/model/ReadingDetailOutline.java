package com.king.reading.model;

import com.king.reading.common.adapter.entity.MultiItemEntity;

/**
 * Created by hu.yang on 2017/6/12.
 */

public class ReadingDetailOutline implements MultiItemEntity {
    public static final int MODULE = 1;
    public static final int UNIT = 2;
    public String content;
    public int mItemType;

    public ReadingDetailOutline(String content, int itemType) {
        this.content = content;
        this.mItemType = itemType;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }

    public void setItemType(int itemType) {
        mItemType = itemType;
    }

}
