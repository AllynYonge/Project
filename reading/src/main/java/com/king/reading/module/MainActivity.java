package com.king.reading.module;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.EmptyUtils;
import com.google.common.collect.Lists;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.SysApplication;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.common.utils.AppScreenMgr;
import com.king.reading.common.utils.AppUtils;
import com.king.reading.common.utils.ShareDialog;
import com.king.reading.module.extension.ExtensionFragment;
import com.king.reading.module.learn.LearnFragment;
import com.king.reading.module.read.MainPagerAdapter;
import com.king.reading.module.read.ReadFragment;
import com.king.reading.module.user.ProfileActivity;
import com.king.reading.widget.drawer.Drawer;
import com.king.reading.widget.drawer.DrawerBuilder;
import com.king.reading.widget.drawer.model.PrimaryDrawerItem;
import com.king.reading.widget.drawer.model.interfaces.IDrawerItem;
import com.king.reading.widget.drawer.view.BezelImageView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by hu.yang on 2017/5/25.
 */

@Route(path = C.ROUTER_MAIN)
public class MainActivity extends BaseActivity implements Drawer.OnDrawerItemClickListener {
    private Drawer result;
    private ViewGroup mNavigationView;
    private ViewGroup mContentView;
    private BezelImageView mDrawerHeader;

    @BindView(R.id.tab_bottom_menu)
    public TabLayout tableLayout;
    @BindView(R.id.pager)
    public ViewPager pager;

    @Autowired()
    public long bookId;
    private int[] icons = {R.drawable.ic_main_hand, R.drawable.ic_main_learn, R.drawable.ic_main_extend,};

    private List<String> titleList;

    private List<Fragment> fragmentList = Lists.newArrayList();

    @Override
    public void onInitData(Bundle savedInstanceState) {

    }

    @Override
    public void onInitView() {
        ARouter.getInstance().inject(this);
        TitleBuilder.build(this).setShowTitleBar(false).init();
        fragmentList.add(ReadFragment.newInstance(bookId));
        fragmentList.add(new LearnFragment());
        fragmentList.add(new ExtensionFragment());
        View headerView = getLayoutInflater().inflate(R.layout.draw_main_header, null);
        mDrawerHeader = (BezelImageView) headerView.findViewById(R.id.material_drawer_account_header_current);
        mDrawerHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        AppUtils.setHeadImage(mDrawerHeader, this);//设置头像
        result = new DrawerBuilder()
                .withActivity(this)
                .withSliderBackgroundColorRes(R.color.white)
                .withTranslucentStatusBar(true)
                .withHeader(headerView)
                .withDrawerItems(initDrawItem())
                .withSelectedItemByPosition(-1)
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View view) {}

                    @Override
                    public void onDrawerClosed(View view) {}

                    @Override
                    public void onDrawerSlide(View view, float slideOffset) {
                        //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                        mContentView.layout(mNavigationView.getRight(), 0, mNavigationView.getRight() +
                                AppScreenMgr.getScreenWidth(getApplicationContext()), AppScreenMgr.getScreenHeight(getApplicationContext()));
                    }
                })
                .withOnDrawerItemClickListener(this)
                .build();
        //解决菜单页面的状态栏问题
        solutionDrawerStatusBar();

        titleList = Arrays.asList(getResources().getStringArray(R.array.bottom_menu));
        pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragmentList, titleList));
        tableLayout.setupWithViewPager(pager);
        for (int i = 0; i < tableLayout.getTabCount(); i++) {
            tableLayout.getTabAt(i)
                    .setCustomView(R.layout.item_main_indicator)
                    .setIcon(icons[i])
                    .setText(pager.getAdapter().getPageTitle(i)).getCustomView();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }


    private void solutionDrawerStatusBar() {
        mNavigationView = (ViewGroup) result.getDrawerLayout().getChildAt(1);
        //获取侧滑栏的RecyclerView
        View childAt = mNavigationView.getChildAt(1);

        //创建一个View，并把它的高度设为状态栏高度，并添加到侧滑栏中
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, AppScreenMgr.getStatusHeight(getApplicationContext()));
        View view = new View(getApplicationContext());
        view.setBackgroundResource(R.color.colorPrimary);
        view.setId(R.id.material_statusBar_id);
        view.setLayoutParams(params);
        mNavigationView.addView(view);

        //把RecyclerView设置为View的下面
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) childAt.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.material_statusBar_id);
        childAt.setLayoutParams(layoutParams);
        mContentView = (ViewGroup) result.getDrawerLayout().getChildAt(0);
    }

    @Override
    public void onBackPressed() {
        if (result.isDrawerOpen()) {
            result.closeDrawer();
            return;
        }
        super.onBackPressed();
    }

    private List<IDrawerItem> initDrawItem() {
        List<IDrawerItem> items = new ArrayList<>();
        items.add(new PrimaryDrawerItem().withName("更换课本")
                .withDescription("广州版三年级下册")
                .withDescriptionTextColorRes(R.color.gray)
                .withIcon(R.mipmap.ic_menu_order));
        items.add(new PrimaryDrawerItem().withName("会员服务")
                .withDescription("使用时间还有15天")
                .withDescriptionTextColorRes(R.color.gray)
                .withIcon(R.mipmap.ic_menu_vip));
        items.add(new PrimaryDrawerItem().withName("消息与活动")
                .withIcon(R.mipmap.ic_menu_message)
                .withBadge(""));
        items.add(new PrimaryDrawerItem().withName("设置")
                .withIcon(R.mipmap.ic_menu_setting));
        items.add(new PrimaryDrawerItem().withName("帮助与反馈")
                .withIcon(R.mipmap.ic_menu_feedback));
        items.add(new PrimaryDrawerItem().withName("客服")
                .withIcon(R.mipmap.ic_menu_service));
        items.add(new PrimaryDrawerItem().withName("分享APP")
                .withIcon(R.mipmap.ic_menu_share));
        return items;
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        switch (position) {
            case 1:
                getNavigation().routerUpdateVersionAct(bookId);
                break;
            case 2:
                getNavigation().routerPayFeatureAct();
                break;
            case 3:
                getNavigation().routerNoticeAct();
                break;
            case 4:
                getNavigation().routerSettingAct();
                break;
            case 5:
                getNavigation().routerFeedBackAct();
                break;
            case 6:
                getNavigation().routerCustomerAct();
                break;
            case 7:
//                getNavigation().routerCustomerAct();
                showShareDialog();
                break;
        }
        return false;
    }



    @Override
    public boolean hasEventBus() {
        return true;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Integer event) {
        //头像上传成功的通知
        if (event == C.EADIMAGE_IS_UPDATE) {
            AppUtils.setHeadImage(mDrawerHeader, this);
        }
    }

    private void showShareDialog() {
        final ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.dismiss();
            }
        });

        shareDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);

                Platform.ShareParams sp = new Platform.ShareParams();
                if (item.get("ItemText").equals("QQ")) {
                    Toast.makeText(SysApplication.getApplication(), "QQ", Toast.LENGTH_SHORT).show();
                    sp.setTitle("点读宝助你开心学英语");//标题
                    sp.setText("轻松完成英语听读作业，自信说出纯正英语");//描述
                    sp.setTitleUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.king.reading"); // 标题的超链接
                    sp.setImageUrl("http://ddb.kingsun.cn/" + "image/share_qq.png");// 分享网络图片
                    Platform qq = ShareSDK.getPlatform(QQ.NAME);
                    qq.setPlatformActionListener(myPlatformActionListener); // 设置分享事件回调
                    // 执行分享
                    qq.share(sp);
                } else if (item.get("ItemText").equals("QQ空间")) {

                    sp.setTitle("点读宝助你开心学英语");//标题
                    sp.setTitleUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.king.reading"); // 标题的超链接
                    sp.setText("轻松完成英语听读作业，自信说出纯正英语");//描述
                    sp.setImageUrl("http://ddb.kingsun.cn/" + "image/share_qq.png");// 分享网络图片

                    Platform qq = ShareSDK.getPlatform(QZone.NAME);
                    qq.setPlatformActionListener(myPlatformActionListener); // 设置分享事件回调
                    // 执行分享
                    qq.share(sp);

                } else if (item.get("ItemText").equals("微信好友")) {

                    sp.setText("图片");//描述

                    sp.setImageUrl("http://ddb.kingsun.cn/" + "image/share_wechat.png");

                    sp.setShareType(Platform.SHARE_IMAGE);//非常重要：一定要设置分享属性 图文分享
                    Platform qq = ShareSDK.getPlatform(Wechat.NAME);

                    qq.setPlatformActionListener(myPlatformActionListener); // 设置分享事件回调
                    // 执行分享
                    qq.share(sp);

                } else if (item.get("ItemText").equals("微信朋友圈")) {

                    sp.setTitle("微信朋友圈");//标题
                    sp.setText("图片");//描述

                    sp.setImageUrl("http://ddb.kingsun.cn/" + "image/share_wechat.png");
                    sp.setShareType(Platform.SHARE_IMAGE);//非常重要：一定要设置分享属性 图文分享
                    Platform qq = ShareSDK.getPlatform(WechatMoments.NAME);

                    qq.setPlatformActionListener(myPlatformActionListener); // 设置分享事件回调
                    // 执行分享
                    qq.share(sp);
                }
                shareDialog.dismiss();
            }
        });
    }

    PlatformActionListener myPlatformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            Toast.makeText(SysApplication.getApplication(), "分享成功", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {

            Toast.makeText(SysApplication.getApplication(), "分享失败", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(Platform platform, int i) {
            Toast.makeText(SysApplication.getApplication(), "取消分享", Toast.LENGTH_LONG).show();
        }
    };

    public void openDrawer() {
        if (EmptyUtils.isNotEmpty(result))
            result.openDrawer();
    }
}
