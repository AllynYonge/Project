package com.king.reading.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.king.reading.R;
import com.king.reading.common.utils.BitmapUtils;
import com.king.reading.data.entities.PageEntity;
import com.king.reading.ddb.Page;
import com.king.reading.encyption.glide.EncryptionFileToStreamDecoder;
import com.king.reading.widget.PageLayout;
import com.king.reading.widget.ReadingViewPager;

import java.io.File;
import java.util.List;


/**
 * 创建者     王开冰
 * 创建时间   2017/7/5 17:58
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class ReadingPagerAdapter extends PagerAdapter {
    private final EncryptionFileToStreamDecoder decoder;
    private String TAG = "ReadingPagerAdapter";
    List<PageEntity> mGetPageResponse;

    private LayoutInflater mLayoutinflater;
    private Activity mContext;
    private Page mPage;
    private ReadingViewPager mReadingViewPager;
    private Handler mHandler;

    public ReadingPagerAdapter(Activity context, List<PageEntity> getPageResponse, ReadingViewPager readingViewPager, EncryptionFileToStreamDecoder decoder, Handler handler) {
        mContext = context;
        this.decoder = decoder;
        mGetPageResponse = getPageResponse;
        mReadingViewPager = readingViewPager;
        mHandler = handler;
        mLayoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mGetPageResponse != null) {
            return mGetPageResponse.size();
            //            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        String thread = Thread.currentThread().toString();
        Log.d(TAG, "thread=" + thread);
        final ViewGroup layout;
        layout = (ViewGroup) mLayoutinflater.inflate(R.layout.item_reading_page, null);
        final ImageView iv = (ImageView) layout.findViewById(R.id.image_reading_textbook);
        PageLayout pageLayout = (PageLayout) layout.findViewById(R.id.pageLayout_item_reading_textbook);

        mPage = mGetPageResponse.get(position).page;

        //        Glide.with(mContext).load(mPage.encryptImageURL).into(iv);
        new getImageCacheAsyncTask(mContext, pageLayout, mPage, iv, position).execute("http://fzresource.oss-cn-shenzhen.aliyuncs.com/dev/source/textbooks/GDBXXYYLNJSC/dub/page008/bg.jpg");
        Glide.with(mContext).load(mPage.encryptImageURL).diskCacheStrategy(DiskCacheStrategy.SOURCE).cacheDecoder(decoder).into(iv);
        /*Glide.with(mContext).load("http://192.168.3.197:8080/share_app.png").asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                iv.setImageBitmap(resource);
            }
        });*/
        layout.setId(position);
        layout.setTag(pageLayout);
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                container.addView(layout);

            }
        });
        mReadingViewPager.setObjectForPosition(layout, position);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private class getImageCacheAsyncTask extends AsyncTask<String, Void, File> {
        private final Context context;
        private PageLayout mPageLayout;
        private Page mPage;
        private ImageView mImageView;
        private int mPosition;

        public getImageCacheAsyncTask(Context context, PageLayout pageLayout, Page page, ImageView iv, int position) {
            this.context = context;
            mPageLayout = pageLayout;
            mPage = page;
            mImageView = iv;
            mPosition = position;
        }

        @Override
        protected File doInBackground(String... params) {
            String imgUrl =  params[0];
            try {
                return Glide.with(context)
                        .load(imgUrl)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            //此path就是对应文件的缓存路径
            String path = result.getPath();
            Uri imageUri = Uri.parse("file://" + path);
            Bitmap bitmap = BitmapUtils.decodeUriAsBitmap(imageUri, context);
            //mImageView.setImageBitmap(bitmap);
            mPageLayout.setConfigs(mPage, imageUri, mHandler, mPosition);
        }
    }
}
