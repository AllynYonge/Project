package com.king.reading.widget.drawer.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.king.reading.R;

public abstract class AbstractDrawerImageLoader implements DrawerImageLoader.IDrawerImageLoader {
    @Override
    public void set(ImageView imageView, Uri uri, Drawable placeholder) {
    }

    @Override
    public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
        //for backwards compatibility call the method without tag too
        set(imageView, uri, placeholder);
        //this won't do anything
        Log.i("MaterialDrawer", "You have not specified a ImageLoader implementation through the DrawerImageLoader.init() method, or you are still overriding the deprecated method set(ImageView iv, Uri u, Drawable d) instead of set(ImageView iv, Uri u, Drawable d, String tag)");
    }
    @Override
    public void cancel(ImageView imageView) {
    }

    @Override
    public Drawable placeholder(Context ctx) {
        return ctx.getResources().getDrawable(R.drawable.ic_default_person);
    }

    @Override
    public Drawable placeholder(Context ctx, String tag) {
        return placeholder(ctx);
    }
}
