package com.king.reading.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.king.reading.R;
import com.king.reading.common.utils.AppUtils;
import com.king.reading.ddb.MissionItem;
import com.king.reading.model.Follow;

import java.util.List;


/**
 * 创建者     王开冰
 * 创建时间   2017/7/29 10:52
 * 描述	      跟读闯关模块的适配器
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

public class BreakThroughDetailPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<MissionItem> mMissionItems;
    private List<Follow> mFollows;

    public BreakThroughDetailPagerAdapter(Context context, List<MissionItem> missionItems, List<Follow> follows) {
        mContext = context;
        mMissionItems = missionItems;
        mFollows = follows;
    }

    @Override
    public int getCount() {
        if (mMissionItems != null) {
            return mMissionItems.size();
        }
        return 0;
    }

    public void setWordResultColor(List<Follow> follows) {
        mFollows = follows;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_follow_word, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_follow_word);
        TextView textView = (TextView) view.findViewById(R.id.tv_follow_word);
        view.setTag(position);
        Glide.with(mContext).load(mMissionItems.get(position).imageURL).into(imageView);
        setText(textView, position);

        container.addView(view);
        return view;
    }

    private void setText(TextView textView, int position) {
        textView.setText(mMissionItems.get(position).word);
        if (mFollows.size() > position && mFollows.get(position).getWords().size() > 0) {
            AppUtils.setViewTextColor(textView, mFollows.get(position).getWords());
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
