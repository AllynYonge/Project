package com.king.reading.module.extension;

import android.os.Bundle;
import android.view.View;

import com.google.common.collect.Lists;
import com.king.reading.R;
import com.king.reading.base.activity.RecyclerViewActivity;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.model.ExtensionModule;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by AllynYonge on 24/06/2017.
 */

public class AddExtensionActivity extends RecyclerViewActivity<ExtensionModule>{
    private List<ExtensionModule> modules;
    @Override
    public void onInitData(Bundle savedInstanceState) {
    }

    @Override
    public void onInitView() {
        super.onInitView();
        setLeftImageIcon(R.mipmap.ic_back);
        setCenterTitle("添加扩展");
        setEnableRefresh(false);
    }

    @Override
    public BaseQuickAdapter<ExtensionModule, BaseViewHolder> getAdapter() {
        return new BaseQuickAdapter<ExtensionModule, BaseViewHolder>(R.layout.item_addmodule) {
            @Override
            protected void convert(BaseViewHolder helper, ExtensionModule item) {
                helper.setText(R.id.tv_addmodule_name, item.extensionName);
                helper.addOnClickListener(R.id.tv_addmodule_name);
                if (item.isAdd){
                    helper.setText(R.id.tv_addmodule_add, "删除");
                } else {
                    helper.setText(R.id.tv_addmodule_add, "添加");
                }
            }
        };
    }

    @Override
    public INetPresenter getPresenter() {
        return new INetPresenter() {
            @Override
            public void onRefresh() {
                modules = Lists.newArrayList();
                modules.add(new ExtensionModule("洋葱数学", true , 0));
                modules.add(new ExtensionModule("电影课题", true , 0));
                modules.add(new ExtensionModule("语文天地", false , 0));
                modules.add(new ExtensionModule("自然拼读", true , 0));
                refreshUI(modules);
            }

            @Override
            public void onLoadMore(IBaseRecyclerView netView) {

            }
        };
    }

    @Override
    public int getVerticalInterval() {
        return 0;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ExtensionModule item = (ExtensionModule) adapter.getItem(position);
        item.isAdd = !item.isAdd;
        int type = item.isAdd ? 2 : 1;
        switch (view.getId()){
            case R.id.tv_addmodule_name:
                getUserRepository().updateExtension(type, item.extensionId).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean isSuccess) throws Exception {
                        refreshUI(modules);
                    }
                });
                break;
        }
    }
}
