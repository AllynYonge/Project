package com.king.reading.common.adapter.entity;

import java.util.List;

/**
 * implement the interface if the item is expandable
 * Created by luoxw on 2016/8/8.
 */
public interface IExpandAnimAble<T> {
    boolean isExpanded();
    void setExpanded(boolean expanded);
    int getOrientation();
    List<T> getItems();
}
