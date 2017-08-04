package com.king.reading.base.activity;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.king.reading.Navigation;
import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.common.utils.AsaSystemBar;
import com.king.reading.common.utils.DialogUtils;
import com.king.reading.data.repository.OtherRepository;
import com.king.reading.data.repository.ResRepository;
import com.king.reading.data.repository.UserRepository;
import com.king.reading.injector.AppComponent;
import com.king.reading.widget.statelayout.StatefulLayout;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hu.yang on 2017/4/25.
 */

public abstract class BaseActivity extends RxAppCompatActivity implements View.OnClickListener{

    @Inject
    Application application;
    @Inject Navigation navigation;
    @Inject ResRepository resRepository;
    @Inject OtherRepository otherRepository;
    @Inject UserRepository userRepository;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_toolbar_title)
    TextView mTitle;
    @BindView(R.id.tv_toolbar_right)
    TextView mOperation;
    @BindView(R.id.image_toolbar_left)
    ImageView mLeftImage;
    @BindView(R.id.image_toolbar_right)
    ImageView mRightImage;
    @BindView(R.id.image_toolbar_assist_right)
    ImageView mRightAssistImage;
    @BindView(R.id.tab_toolbar_multiTab)
    TabLayout mTabLayout;
    @BindView(R.id.stateLayout)
    StatefulLayout mStatefulLayout;

    private LinearLayout mRootView;
    private MaterialDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppComponent().inject(this);

        //stateBar处理
        AsaSystemBar.from(this).setUseBelow(Build.VERSION_CODES.LOLLIPOP)
                .setTransparentStatusBar(true)
                .addStatusBarView(true)
                .setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .process();

        mRootView = (LinearLayout) getLayoutInflater().inflate(R.layout.fragment_base, null);
        getLayoutInflater().inflate(getContentView(), (LinearLayout) mRootView.findViewById(R.id.ll_content), true);
        setContentView(mRootView);
        ButterKnife.bind(this);
        if (hasEventBus()) EventBus.getDefault().register(this);
        onInitData(savedInstanceState);
        onInitView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
        if (hasEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_toolbar_left:
                onLeftClick(v);
                break;
            case R.id.image_toolbar_right:
            case R.id.tv_toolbar_right:
                onRightClick(v);
                break;
            case R.id.tv_toolbar_title:
                onTitleClick(v);
                break;
            case R.id.image_toolbar_assist_right:
                onRightLeftclick(v);
                break;
        }
    }

    public AppComponent getAppComponent(){
        return ((SysApplication) getApplication()).getAppComponent();
    }

    public OtherRepository getOtherRepository() {
        return otherRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ImageView getLeftImage() {
        return mLeftImage;
    }

    public ImageView getRightImage() {
        return mRightImage;
    }

    public ImageView getRightAssistImage() {
        return mRightAssistImage;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public ResRepository getResRepository(){
        return resRepository;
    }

    public void setTabLayout(ViewPager pager) {
        mTabLayout.setVisibility(View.VISIBLE);
        mTabLayout.setupWithViewPager(pager);
        mTitle.setVisibility(View.GONE);
    }

    public void setCenterTitle(String title) {
        mTabLayout.setVisibility(View.GONE);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(title);
        mTitle.setOnClickListener(this);
    }

    public void setTitleRightIcon(@DrawableRes int iconRes) {
        Drawable drawable = getResources().getDrawable(iconRes);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTitle.setCompoundDrawables(null, null, drawable, null);
        mTitle.setCompoundDrawablePadding(16);
    }

    public void setRightText(String operation) {
        mOperation.setVisibility(View.VISIBLE);
        mOperation.setText(operation);
        mOperation.setOnClickListener(this);
    }

    public void setLeftImageIcon(@DrawableRes int iconRes) {
        mLeftImage.setVisibility(View.VISIBLE);
        mLeftImage.setImageResource(iconRes);
        mLeftImage.setOnClickListener(this);
    }

    public void setRightImageIcon(@DrawableRes int iconRes) {
        mRightImage.setVisibility(View.VISIBLE);
        mRightImage.setImageResource(iconRes);
        mRightImage.setOnClickListener(this);
    }

    public void setRightAssistImageIcon(@DrawableRes int iconRes) {
        mRightAssistImage.setVisibility(View.VISIBLE);
        mRightAssistImage.setImageResource(iconRes);
        mRightAssistImage.setOnClickListener(this);
    }

    private void configTitleBar(TitleBuilder titleBuilder) {
        mToolbar.setVisibility(titleBuilder.showTitleBar ? View.VISIBLE : View.GONE);

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

        if (titleBuilder.pager != null){
            mTabLayout.setVisibility(View.VISIBLE);
            mTabLayout.setupWithViewPager(titleBuilder.pager);
            mTitle.setVisibility(View.GONE);
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

    public void showContent() {
        mStatefulLayout.showContent();
    }

    public void showLoading() {
        mStatefulLayout.showLoading();
    }

    public void showEmpty() {
        mStatefulLayout.showEmpty();
    }

    public void showError(View.OnClickListener clickListener) {
        mStatefulLayout.showError("服务器出错，请点击重试", clickListener);
    }

    public void showOffline(View.OnClickListener clickListener) {
        mStatefulLayout.showOffline("您的手机网络不太畅通哦~", clickListener);
    }

    public void showProgressDialog(String content) {
        dismissProgressDialog();
       if (progressDialog == null) {
            progressDialog = DialogUtils.showProgressDialog(this, content);
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void onLeftClick(View v){
        finish();
    }
    public void onRightClick(View v){}
    public void onTitleClick(View v){}
    public void onRightLeftclick(View v){}

    public boolean hasEventBus(){
        return false;
    }

    public abstract void onInitData(Bundle savedInstanceState);

    public abstract void onInitView();

    public abstract int getContentView();

    public static class TitleBuilder{
        private final BaseActivity activity;
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

        private TitleBuilder(BaseActivity activity) {
            this.activity = activity;
        }

        public static TitleBuilder build(BaseActivity activity){
            return new TitleBuilder(activity);
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
            activity.configTitleBar(this);
        }
    }

}
