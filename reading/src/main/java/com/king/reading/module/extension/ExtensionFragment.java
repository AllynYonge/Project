package com.king.reading.module.extension;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.king.reading.R;
import com.king.reading.base.fragment.RecyclerFragment;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.common.adapter.util.GridItemColumnDecoration;
import com.king.reading.model.ExtensionBlock;
import com.king.reading.widget.drawer.util.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu.yang on 2017/6/13.
 */

public class ExtensionFragment extends RecyclerFragment<ExtensionBlock>{
    private List<ExtensionBlock> blockList;

    @Override
    public BaseQuickAdapter<ExtensionBlock, BaseViewHolder> getAdapter() {
        return new BaseQuickAdapter<ExtensionBlock, BaseViewHolder>(R.layout.item_extension, blockList) {
            @Override
            protected void convert(BaseViewHolder helper, ExtensionBlock item) {
                helper.setImageResource(R.id.image_extension_module, item.url);
                helper.setText(R.id.tv_extension_moduleName, item.moduleName);
                if (item.isAdd){
                    helper.setVisible(R.id.tv_extension_moduleName,false);
                }
            }
        };
    }

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {
                blockList.clear();
                blockList.add(new ExtensionBlock(R.mipmap.ic_add,"","", true));
                blockList.add(new ExtensionBlock(R.mipmap.bg_learn_listen, "电影课","", false));
                blockList.add(new ExtensionBlock(R.mipmap.bg_learn_roleplay, "自然拼读","", false));
                ExtensionFragment.this.refreshUI(blockList);
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
                .setCenterTitle("扩展课")
                .setLeftIcon(R.mipmap.ic_avatar)
                .init();
        getRecyclerView().addItemDecoration(new GridItemColumnDecoration(2, DensityUtils.dp2px(getActivity().getApplicationContext(),12)));
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_main;
    }
}
