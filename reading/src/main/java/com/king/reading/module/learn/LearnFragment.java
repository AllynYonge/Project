package com.king.reading.module.learn;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.base.activity.BaseActivity;
import com.king.reading.base.fragment.RecyclerFragment;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.common.adapter.util.GridItemColumnDecoration;
import com.king.reading.data.entities.ReadAfterMeEntity;
import com.king.reading.model.ExtensionBlock;
import com.king.reading.widget.drawer.util.DensityUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by hu.yang on 2017/6/6.
 */

public class LearnFragment extends RecyclerFragment<ExtensionBlock>{
    private List<ExtensionBlock> blockList = Lists.newArrayList();
    @Override
    public int getContentView() {
        return R.layout.fragment_main;
    }

    @Override
    public BaseQuickAdapter<ExtensionBlock, BaseViewHolder> getAdapter() {
        return new BaseQuickAdapter<ExtensionBlock, BaseViewHolder>(R.layout.item_extension, blockList) {
            @Override
            protected void convert(BaseViewHolder helper, ExtensionBlock item) {
                helper.setImageResource(R.id.image_extension_module, item.url);
                helper.setText(R.id.tv_extension_moduleName, item.moduleName);
                helper.addOnClickListener(R.id.ll_extension);
            }
        };
    }

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {
                blockList.clear();
                blockList.add(new ExtensionBlock(R.mipmap.bg_learn_listen, "随身听", C.ROUTER_LEARN_LISTEN, false));
                blockList.add(new ExtensionBlock(R.mipmap.bg_learn_word, "单词拼写", C.ROUTER_LEARN_WORDLISTEN, false));
                blockList.add(new ExtensionBlock(R.mipmap.bg_learn_roleplay, "角色扮演", C.ROUTER_LEARN_ROLEPLAY, false));
                LearnFragment.this.refreshUI(blockList);

                BaseActivity.class.cast(getActivity()).getUserRepository().getTotalStart().subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        if (getRecyclerAdapter().getHeaderLayoutCount() > 0){
                            TextView totalStar = (TextView) getRecyclerAdapter().getHeaderLayout().findViewById(R.id.tv_learn_totalStar);
                            totalStar.setText("已获得" + integer + "颗星星");
                        }
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
        return new GridLayoutManager(getActivity().getApplicationContext(), 2);
    }

    @Override
    public void onInitData(Bundle savedInstanceState) {
        blockList = new ArrayList<>();
    }

    @Override
    public void onInitView() {
        super.onInitView();
        TitleBuilder.build(this)
                .setCenterTitle("同步学")
                .setLeftIcon(R.mipmap.ic_avatar)
                .init();
        getRecyclerAdapter().addHeaderView(getActivity().getLayoutInflater().inflate(R.layout.item_header_learn, null));
        getRecyclerAdapter().getHeaderLayout().findViewById(R.id.image_learn_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.class.cast(getActivity()).getNavigation().routerBreakThrough();
            }
        });
        getRecyclerView().addItemDecoration(new GridItemColumnDecoration(2, DensityUtils.dp2px(getActivity().getApplicationContext(),12)));
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ExtensionBlock block = (ExtensionBlock) adapter.getItem(position);
        switch (view.getId()){
            case R.id.ll_extension:
                switch (block.skipUrl){
                    case C.ROUTER_LEARN_LISTEN:
                        BaseActivity.class.cast(getActivity()).getNavigation().routerListenAct();
                        break;
                    case C.ROUTER_LEARN_WORDLISTEN:
                        BaseActivity.class.cast(getActivity()).getNavigation().routerWordListenAct();
                        break;
                    case C.ROUTER_LEARN_ROLEPLAY:
                        BaseActivity.class.cast(getActivity()).getNavigation().routerRolePlayAct();
                        break;
                }
                break;
        }
    }
}
