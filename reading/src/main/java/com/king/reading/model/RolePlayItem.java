package com.king.reading.model;

import com.king.reading.common.adapter.entity.MultiItemEntity;

/**
 * Created by hu.yang on 2017/6/19.
 */

public class RolePlayItem implements MultiItemEntity{
    public final static int MINE = 0;
    public final static int OTHER = 1;
    public int type;
    public String avatarUrl;
    public String content;

    public RolePlayItem(int type, String avatarUrl, String content) {
        this.type = type;
        this.avatarUrl = avatarUrl;
        this.content = content;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
