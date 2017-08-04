package com.king.reading.widget.drawer.model.interfaces;


import com.king.reading.widget.drawer.holder.StringHolder;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface Badgeable<T> {
    T withBadge(String badge);

    T withBadge(int badgeRes);

    T withBadge(StringHolder badgeRes);

    StringHolder getBadge();
}
