package com.king.reading.model;

import com.king.reading.common.adapter.entity.MultiItemEntity;

/**
 * Created by AllynYonge on 14/06/2017.
 */

public class SortList implements MultiItemEntity {
    public final static int MINE = 0;
    public final static int FIRST = 1;
    public final static int SECOND = 2;
    public final static int THIRD = 3;
    public final static int DEFAULT = 4;

    public String number;
    public String name;
    public String starNum;
    public String passNum;
    public int type;

    public SortList(String number, String name, String starNum, String passNum, int type) {
        this.number = number;
        this.name = name;
        this.starNum = starNum;
        this.passNum = passNum;
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
