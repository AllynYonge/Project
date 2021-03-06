package com.king.reading.widget.drawer.model.interfaces;

import android.graphics.Typeface;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface Typefaceable<T> {
    T withTypeface(Typeface typeface);

    Typeface getTypeface();
}
