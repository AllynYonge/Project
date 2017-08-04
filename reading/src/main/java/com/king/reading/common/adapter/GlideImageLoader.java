package com.king.reading.common.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.king.reading.data.entities.BannerEntity;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by AllynYonge on 20/07/2017.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object entity, ImageView imageView) {
        //Glide 加载图片简单用法
        String url = ((BannerEntity)entity).bannerUrl;
        Glide.with(context).load(url).into(imageView);
    }
}