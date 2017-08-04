package com.king.reading.module.user;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.king.reading.C;
import com.king.reading.Navigation;
import com.king.reading.R;
import com.king.reading.base.activity.RecyclerViewActivity;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.common.utils.DialogUtils;
import com.king.reading.common.utils.ToastUtils;
import com.king.reading.data.entities.BookBaseEntity;
import com.king.reading.data.repository.ResRepository;
import com.king.reading.data.repository.UserRepository;
import com.king.reading.widget.drawer.util.DensityUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by AllynYonge on 16/06/2017.
 */

@Route(path = C.ROUTER_SELECTCLASS)
public class SelectClassActivity extends RecyclerViewActivity<BookBaseEntity>{
    @Inject ResRepository resRepository;
    @Inject UserRepository userRepository;
    @Inject Navigation navigation;
    @Autowired
    public String areaName;
    @BindView(R.id.tv_bookversion_prompt) TextView mPrompt;
    @Override
    public void onInitData(Bundle savedInstanceState) {
        getAppComponent().plus().inject(this);
        ARouter.getInstance().inject(this);
    }

    @Override
    public void onInitView() {
        super.onInitView();
        areaName = EmptyUtils.isEmpty(areaName) ? getUserRepository().getAreaNameForId(userRepository.getUser().usingBook) : areaName;
        setCenterTitle(areaName);
        setTitleRightIcon(R.mipmap.ic_arrow_down);
    }

    @Override
    public BaseQuickAdapter<BookBaseEntity, BaseViewHolder> getAdapter() {
        return new BaseQuickAdapter<BookBaseEntity, BaseViewHolder>(R.layout.item_bookgrade) {
            @Override
            protected void convert(BaseViewHolder helper, BookBaseEntity item) {
                helper.addOnClickListener(R.id.rl_bookGrade);
                helper.setText(R.id.tv_bookversion_bookName, item.bookName);
                ImageView bookCover = helper.getView(R.id.image_book);
                Glide.with(SelectClassActivity.this).load(item.coverURL)
                        .placeholder(R.mipmap.ic_book_default)
                        .override(257,365)
                        .into(bookCover);
            }
        };
    }

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {
                resRepository.getBooksForVersion(areaName).subscribe(new Consumer<List<BookBaseEntity>>() {
                    @Override
                    public void accept(@NonNull List<BookBaseEntity> books) throws Exception {
                        refreshUI(books);
                    }
                });
            }

            @Override
            public void onLoadMore(IBaseRecyclerView netView) {

            }
        };
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(FlexDirection.ROW, FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.SPACE_BETWEEN);
        return layoutManager;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_bookversions;
    }

    public void showPrompt(){
        mPrompt.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        BookBaseEntity item = (BookBaseEntity) adapter.getItem(position);
        switch (view.getId()){
            case R.id.rl_bookGrade:
                final long bookId = item.bookId;
                showProgressDialog("正在更新信息，请稍后...");
                userRepository.updateUserInfo("","",bookId,0l,0,"").subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        dismissProgressDialog();
                        //预加载书本信息
                        //resRepository.getBookDetail(bookId).subscribe();
                        //跳转主页
                        navigation.routerMainAct(bookId);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissProgressDialog();
                        ToastUtils.show("更新信息失败，请重试");
                    }
                });
                break;
        }
    }

    @Override
    public void onTitleClick(View v) {
        resRepository.getAllVersions().subscribe(new Consumer<List<BookBaseEntity>>() {
            @Override
            public void accept(@NonNull List<BookBaseEntity> books) throws Exception {
                List<String> list = new ArrayList<String>();
                for (BookBaseEntity book : books){
                    list.add(book.areaName);
                }
                DialogUtils.showListDialog(SelectClassActivity.this, list, "选择版本", new DialogUtils.IListDialogItemCallback() {
                    @Override
                    public void onListItemSelected(CharSequence text, int which) {
                        setCenterTitle(text.toString());
                        areaName = text.toString();
                        resRepository.getBooksForVersion((String) text).subscribe(new Consumer<List<BookBaseEntity>>() {
                            @Override
                            public void accept(@NonNull List<BookBaseEntity> books) throws Exception {
                                refreshUI(books);
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    public int getVerticalInterval() {
        return DensityUtils.dp2px(getApplicationContext(), 20);
    }
}
