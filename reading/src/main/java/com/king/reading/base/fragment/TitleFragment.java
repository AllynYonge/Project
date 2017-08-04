package com.king.reading.base.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.reading.R;
import com.king.reading.widget.statelayout.StatefulLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hu.yang on 2017/6/8.
 */

public abstract class TitleFragment extends Fragment{

    private LinearLayout mRootView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tv_toolbar_title) TextView mTitle;
    @BindView(R.id.tv_toolbar_right) TextView mOperation;
    @BindView(R.id.image_toolbar_left) ImageView mLeftImage;
    @BindView(R.id.image_toolbar_right) ImageView mRightImage;
    @BindView(R.id.image_toolbar_assist_right) ImageView mRightAssistImage;
    @BindView(R.id.stateLayout) StatefulLayout mStatefulLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = (LinearLayout) inflater.inflate(R.layout.fragment_base, null);
            inflater.inflate(getContentView(), (LinearLayout) mRootView.findViewById(R.id.ll_content), true);
            ButterKnife.bind(this, mRootView);
            onInitData(savedInstanceState);
            onInitView();
        }
        return mRootView;
    }

    private void configTitleBar(TitleBuilder titleBuilder) {
        if (titleBuilder.showTitleBar){
            mToolbar.setVisibility(View.VISIBLE);
            mToolbar.setPadding(0,0,0,0);
        } else {
            mToolbar.setVisibility(View.GONE);
        }

        if (!titleBuilder.title.isEmpty()){
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(titleBuilder.title);
            mTitle.setOnClickListener(titleBuilder.centerTitleClickListener);
        }

        if (titleBuilder.centerRightIcon != 0){
            Drawable drawable = getResources().getDrawable(titleBuilder.centerRightIcon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTitle.setCompoundDrawables(null, null, drawable, null);
            mTitle.setCompoundDrawablePadding(16);
        }

        if (titleBuilder.leftIcon != 0){
            mLeftImage.setVisibility(View.VISIBLE);
            mLeftImage.setImageResource(titleBuilder.leftIcon);
            mLeftImage.setOnClickListener(titleBuilder.leftIconClickListener);
        }

        if (titleBuilder.rightIcon != 0){
            mRightImage.setVisibility(View.VISIBLE);
            mRightImage.setImageResource(titleBuilder.rightIcon);
            mRightImage.setOnClickListener(titleBuilder.rightIconClickListener);
        }

        if (titleBuilder.rightAssistIcon != 0){
            mRightAssistImage.setVisibility(View.VISIBLE);
            mRightAssistImage.setImageResource(titleBuilder.rightAssistIcon);
            mRightAssistImage.setOnClickListener(titleBuilder.rightAssistIconClickListener);
        }
    }

    public LinearLayout getRootView(){
        return mRootView;
    }

    public void showContent(){
        mStatefulLayout.showContent();
    }

    public void showLoading(){
        mStatefulLayout.showLoading();
    }

    public void showEmpty(){
        mStatefulLayout.showEmpty();
    }

    public void showEmpty(String text, View.OnClickListener clickListener){
        mStatefulLayout.showError(text, clickListener);
    }

    public void showOffline(View.OnClickListener clickListener){
        mStatefulLayout.showOffline("您的手机网络不太畅通哦~", clickListener);
    }

    public abstract int getContentView();

    public abstract void onInitData(Bundle savedInstanceState);

    public abstract void onInitView();

    public static class TitleBuilder{
        private final TitleFragment fragment;
        public boolean showTitleBar = true;
        public ViewPager pager;
        public String title = "";
        public View.OnClickListener centerTitleClickListener;
        public int centerRightIcon;
        public int rightIcon;
        public int rightAssistIcon;
        public int leftIcon;
        public View.OnClickListener rightIconClickListener;
        public View.OnClickListener rightAssistIconClickListener;
        public View.OnClickListener leftIconClickListener;

        private TitleBuilder(TitleFragment fragment) {
            this.fragment = fragment;
        }

        public static TitleBuilder build(TitleFragment fragment){
            return new TitleBuilder(fragment);
        }

        public TitleBuilder setShowTitleBar(boolean showTitleBar){
            this.showTitleBar = showTitleBar;
            return this;
        }

        public TitleBuilder setTabLayout(ViewPager pager){
            this.pager = pager;
            return this;
        }

        public TitleBuilder setCenterTitle(String title){
            this.title = title;
            return this;
        }

        public TitleBuilder setCenterTitleClickListener(View.OnClickListener centerTitleClickListener){
            this.centerTitleClickListener = centerTitleClickListener;
            return this;
        }

        public TitleBuilder setCenterRightIcon(@DrawableRes int iconRes){
            this.centerRightIcon = iconRes;
            return this;
        }

        public TitleBuilder setRightIcon(@DrawableRes int iconRes){
            this.rightIcon = iconRes;
            return this;
        }

        public TitleBuilder setRightIconClickListener(View.OnClickListener rightIconClickListener){
            this.rightIconClickListener = rightIconClickListener;
            return this;
        }

        public TitleBuilder setRightAssistIcon(@DrawableRes int iconRes){
            this.rightAssistIcon = iconRes;
            return this;
        }

        public TitleBuilder setRightAssistIconClickListener(View.OnClickListener rightAssistIconClickListener){
            this.rightAssistIconClickListener = rightAssistIconClickListener;
            return this;
        }

        public TitleBuilder setLeftIcon(@DrawableRes int iconRes){
            this.leftIcon = iconRes;
            return this;
        }

        public TitleBuilder setLeftIconClickListener(View.OnClickListener leftIconClickListener){
            this.leftIconClickListener = leftIconClickListener;
            return this;
        }

        public void init(){
            fragment.configTitleBar(this);
        }
    }

}
