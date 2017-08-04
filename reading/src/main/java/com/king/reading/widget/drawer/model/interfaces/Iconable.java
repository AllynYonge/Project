package com.king.reading.widget.drawer.model.interfaces;

import android.graphics.drawable.Drawable;

import com.king.reading.widget.drawer.holder.ImageHolder;


/**
 * Created by mikepenz on 03.02.15.
 */
public interface Iconable<T> {
    T withIcon(Drawable icon);

    T withIcon(ImageHolder icon);

    ImageHolder getIcon();
}
