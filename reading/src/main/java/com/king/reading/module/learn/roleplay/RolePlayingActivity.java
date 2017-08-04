package com.king.reading.module.learn.roleplay;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.base.activity.RecyclerViewActivity;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseExpandableAnimAdapter;
import com.king.reading.common.adapter.BaseLinearGridAdapter;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.data.entities.BookEntity;
import com.king.reading.model.ReadModuleModel;
import com.king.reading.model.ReadUnit;
import com.king.reading.model.ViewMappers;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by hu.yang on 2017/6/13.
 */
@Route(path = C.ROUTER_LEARN_ROLEPLAY)
public class RolePlayingActivity extends RecyclerViewActivity<ReadModuleModel>{
    private List<ReadModuleModel> groupModules;
    private long bookId;

    @Override
    public void onInitData(Bundle savedInstanceState) {
        groupModules = new ArrayList<>();
    }

    @Override
    public void onInitView() {
        super.onInitView();
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("角色扮演");
    }


    @Override
    public BaseQuickAdapter<ReadModuleModel, BaseViewHolder> getAdapter() {
        return new BaseExpandableAnimAdapter<ReadModuleModel>(R.layout.item_textbook, groupModules, BaseLinearGridAdapter.HORIZONTAL, 3) {
            @Override
            protected int getChildLayoutRes() {
                return R.layout.item_textbook_unit;
            }

            @Override
            protected int getToggleExpandId() {
                return R.id.constraint_textbook_item;
            }

            @Override
            protected void bindChildData(ViewGroup childHolder, ReadModuleModel group, int index) {
                final ReadUnit item = group.getItems().get(index);
                TextView unitName = (TextView) childHolder.findViewById(R.id.tv_unit_unitName);
                TextView pageNumber = (TextView) childHolder.findViewById(R.id.tv_unit_pageNumber);
                ImageView unit_intro = (ImageView) childHolder.findViewById(R.id.image_unit_intro);
                Glide.with(RolePlayingActivity.this).load(item.url).into(unit_intro);
                unitName.setText(item.unit);
                pageNumber.setText(item.pageNumber);
                childHolder.findViewById(R.id.card_unit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getNavigation().routerRolePlayUnit(item.unitId);
                    }
                });
            }

            @Override
            protected void convert(BaseViewHolder helper, ReadModuleModel item) {
                helper.setText(R.id.tv_textbook_name, item.moduleName);
                helper.setText(R.id.tv_textbook_pageNumber, item.modulePageNumber);
                super.convert(helper, item);
            }
        };
    }

    @Override
    public int getContentView() {
        return R.layout.activity_role_play;
    }

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {
                if (bookId == 0 && EmptyUtils.isNotEmpty(getUserRepository().getUser())){
                    bookId = getUserRepository().getUser().usingBook;
                }
                getResRepository().getBookDetail(bookId).subscribe(new Consumer<BookEntity>() {
                    @Override
                    public void accept(@NonNull BookEntity bookEntity) throws Exception {
                        groupModules = ViewMappers.mapperRolePlayModule(bookEntity);
                        refreshUI(groupModules);
                    }
                });
            }

            @Override
            public void onLoadMore(IBaseRecyclerView netView) {

            }

        };
    }
}
