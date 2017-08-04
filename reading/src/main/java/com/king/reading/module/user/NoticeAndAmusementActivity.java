package com.king.reading.module.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.base.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by AllynYonge on 20/06/2017.
 */
@Route(path = C.ROUTER_NOTICE)
public class NoticeAndAmusementActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tv_markRead)
    TextView mTvMarkRead;

    private List<String> titles;
    private OnMarkReadOnClickListener mOnMarkReadOnClickListener;

    @Override
    public void onInitData(Bundle savedInstanceState) {
        titles = new ArrayList<>();
        titles.add("通知");
        titles.add("活动");
    }

    @Override
    public void onInitView() {
        setLeftImageIcon(R.mipmap.ic_back);
        pager.setAdapter(new NoticeAndAmuseAdapter(getSupportFragmentManager()));
        setTabLayout(pager);

        pager.setOnPageChangeListener(this);
    }


    @Override
    public int getContentView() {
        return R.layout.activity_notice_amusement;
    }

    @OnClick(R.id.tv_markRead)
    public void markRead(View view){

        mOnMarkReadOnClickListener.markRead();
    }

    public void setOnMarkReadOnClickListener(OnMarkReadOnClickListener onMarkReadOnClickListener){
        mOnMarkReadOnClickListener = onMarkReadOnClickListener;
    }
    public interface OnMarkReadOnClickListener{
        void markRead();
    }

    private class NoticeAndAmuseAdapter extends FragmentPagerAdapter {

        public NoticeAndAmuseAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new NoticeFragment();
                case 1:
                    return new AmusementFragment();
            }
            return null;
        }


        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position==1){
            mTvMarkRead.setVisibility(View.INVISIBLE);
        }else{
            mTvMarkRead.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
