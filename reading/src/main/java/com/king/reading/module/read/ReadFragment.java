package com.king.reading.module.read;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.google.common.collect.Lists;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.base.fragment.RecyclerFragment;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseExpandableAnimAdapter;
import com.king.reading.common.adapter.BaseLinearGridAdapter;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.common.adapter.GlideImageLoader;
import com.king.reading.common.utils.Check;
import com.king.reading.common.utils.ToastUtils;
import com.king.reading.data.entities.BannerEntity;
import com.king.reading.data.entities.BookEntity;
import com.king.reading.data.repository.OtherRepository;
import com.king.reading.data.repository.ResRepository;
import com.king.reading.data.repository.UserRepository;
import com.king.reading.injector.components.DaggerResComponent;
import com.king.reading.injector.components.ResComponent;
import com.king.reading.model.ReadModuleModel;
import com.king.reading.model.ReadUnit;
import com.king.reading.model.ViewMappers;
import com.king.reading.module.MainActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by hu.yang on 2017/6/6.
 */

public class ReadFragment extends RecyclerFragment<ReadModuleModel> {
    @Inject
    ResRepository resRepository;
    @Inject
    OtherRepository otherRepository;
    @Inject
    UserRepository userRepository;
    private List<ReadModuleModel> groupModules = Lists.newArrayList();
    private Banner bannerView;
    private View headView;
    private long bookId;
    private int mStartPage;
    private int mEndPage;
    private boolean mIsVip;

    public static ReadFragment newInstance(long bookId) {
        ReadFragment readFragment = new ReadFragment();

        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putLong(C.INTENT_BOOKID, bookId);
        readFragment.setArguments(argumentsBundle);

        return readFragment;
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        getResComponent().inject(this);
        bookId = getArguments().getLong(C.INTENT_BOOKID);
    }

    @Override
    public void onInitView() {
        super.onInitView();
        TitleBuilder.build(this).setCenterTitle("三年级上册")
                .setLeftIcon(R.mipmap.ic_avatar)
                .setLeftIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.class.cast(getActivity()).openDrawer();
                    }
                })
                .init();
        if (headView == null) {
            headView = getActivity().getLayoutInflater().inflate(R.layout.item_read_header, null);
            bannerView = (Banner) headView.findViewById(R.id.banner);
            //设置banner样式
            bannerView.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //设置图片加载器
            bannerView.setImageLoader(new GlideImageLoader());
            //设置自动轮播，默认为true
            bannerView.isAutoPlay(true);
            //设置轮播时间
            bannerView.setDelayTime(3000);
            //设置指示器位置（当banner模式中有指示器时）
            bannerView.setIndicatorGravity(BannerConfig.RIGHT);
            //banner设置方法全部调用完毕时最后调用
            bannerView.start();
            getRecyclerAdapter().addHeaderView(headView);
        }

    }

    @Override
    public BaseQuickAdapter<ReadModuleModel, BaseViewHolder> getAdapter() {
        return new BaseExpandableAnimAdapter<ReadModuleModel>(R.layout.item_textbook, groupModules, BaseLinearGridAdapter.HORIZONTAL, 2) {
            @Override
            protected int getChildLayoutRes() {
                return R.layout.item_textbook_unit;
            }

            @Override
            protected int getToggleExpandId() {
                return R.id.constraint_textbook_item;
            }

            @Override
            protected void bindChildData(ViewGroup childHolder, final ReadModuleModel group, int index) {
                final ReadUnit item = group.getItems().get(index);
                TextView unitName = (TextView) childHolder.findViewById(R.id.tv_unit_unitName);
                TextView pageNumber = (TextView) childHolder.findViewById(R.id.tv_unit_pageNumber);
                ImageView unit_intro = (ImageView) childHolder.findViewById(R.id.image_unit_intro);
                CardView cardView = (CardView) childHolder.findViewById(R.id.card_unit);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] page = item.pageNumber.split("-");
                        if (Integer.parseInt(page[1]) <= mEndPage){//有权限
                            BaseActivity.class.cast(getActivity()).getNavigation().routerReadDetailAct(mStartPage + "", mEndPage + "", page[0].substring(1));
                        }else{
                            ToastUtils.show("会员用户专属");
                        }
                    }
                });
                Glide.with(ReadFragment.this).load(item.url).into(unit_intro);
                unitName.setText(item.unit);
                pageNumber.setText(item.pageNumber);
            }

            @Override
            protected void convert(BaseViewHolder helper, ReadModuleModel item) {
                super.convert(helper, item);
                helper.setText(R.id.tv_textbook_name, item.moduleName);
                helper.setText(R.id.tv_textbook_pageNumber, item.modulePageNumber);
            }
        };
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_main;
    }

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {
                if (bookId == 0 && EmptyUtils.isNotEmpty(userRepository.getUser())) {
                    bookId = userRepository.getUser().usingBook;
                    mIsVip = userRepository.getUser().vip;
                }

                /*otherRepository.getBanner(1).subscribe(new Consumer<List<BannerEntity>>() {
                    @Override
                    public void accept(@NonNull List<BannerEntity> bannerEntities) throws Exception {
                        if (Check.isNotEmpty(bannerEntities)) {
                            bannerView.update(bannerEntities);
                        } else {
                            getRecyclerAdapter().removeHeaderView(headView);
                        }
                    }
                });*/
                resRepository.getBookDetail(bookId).subscribe(new Consumer<BookEntity>() {
                    @Override
                    public void accept(@NonNull BookEntity bookEntity) throws Exception {
                        mStartPage = bookEntity.startPage;
                        if (mIsVip) {
                            mEndPage = bookEntity.endPage;
                        } else {
                            mEndPage = bookEntity.modules.get(0).end;
                        }
                        refreshUI(ViewMappers.mapperReadModule(bookEntity));
                    }
                });

            }

            @Override
            public void onLoadMore(IBaseRecyclerView netView) {

            }
        };
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (bannerView == null)
            return;

        if (isVisibleToUser)
            bannerView.startAutoPlay();
        else
            bannerView.stopAutoPlay();
    }

    private ResComponent getResComponent() {
        return DaggerResComponent.builder().appComponent(MainActivity.class.cast(getActivity()).getAppComponent()).build();
    }

}
