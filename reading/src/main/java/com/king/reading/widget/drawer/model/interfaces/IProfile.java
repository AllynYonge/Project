package com.king.reading.widget.drawer.model.interfaces;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;

import com.king.reading.widget.drawer.holder.ImageHolder;
import com.king.reading.widget.drawer.holder.StringHolder;
import com.mikepenz.fastadapter.IIdentifyable;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface IProfile<T> extends IIdentifyable<T> {
    T withName(String name);

    StringHolder getName();

    T withEmail(String email);

    StringHolder getEmail();

    T withIcon(Drawable icon);

    T withIcon(Bitmap bitmap);

    T withIcon(@DrawableRes int iconRes);

    T withIcon(String url);

    T withIcon(Uri uri);

    ImageHolder getIcon();

    T withSelectable(boolean selectable);

    boolean isSelectable();
}
