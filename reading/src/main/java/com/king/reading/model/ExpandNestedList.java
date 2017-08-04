package com.king.reading.model;

import com.king.reading.model.BaseNestedList;

import java.util.List;

/**
 * Created by AllynYonge on 15/06/2017.
 */

public class ExpandNestedList<T> extends BaseNestedList<T> {
    public  boolean isExpand;

    public ExpandNestedList(boolean isExpand, List<T> list) {
        super(list);
        this.isExpand = isExpand;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
}
