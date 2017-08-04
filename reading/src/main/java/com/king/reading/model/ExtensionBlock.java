package com.king.reading.model;

import android.support.annotation.DrawableRes;

/**
 * Created by hu.yang on 2017/6/13.
 */

public class ExtensionBlock {
    public @DrawableRes int url;
    public String moduleName;
    public boolean isAdd;
    public String skipUrl;

    public ExtensionBlock(int url, String moduleName, String skipUrl, boolean isAdd) {
        this.url = url;
        this.moduleName = moduleName;
        this.isAdd = isAdd;
        this.skipUrl = skipUrl;
    }
}
