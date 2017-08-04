package com.king.reading.model;

import java.util.List;

/**
 * Created by AllynYonge on 15/06/2017.
 */

public class BaseNestedList<T> {

    public BaseNestedList(List<T> items) {
        this.items = items;
    }

    public List<T> items;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
