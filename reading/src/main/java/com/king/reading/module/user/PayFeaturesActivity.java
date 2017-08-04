package com.king.reading.module.user;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.king.reading.C;
import com.king.reading.R;
import com.king.reading.base.activity.RecyclerViewActivity;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.common.adapter.BaseMultiItemQuickAdapter;
import com.king.reading.common.adapter.BaseQuickAdapter;
import com.king.reading.common.adapter.BaseViewHolder;
import com.king.reading.common.utils.BottomSheetHelper;
import com.king.reading.model.PayFeature;
import com.king.reading.module.vp.presenter.PayPresenter;
import com.king.reading.module.vp.view.PayView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by hu.yang on 2017/6/19.
 */

@Route(path = C.ROUTER_PAYFEATURE)
public class PayFeaturesActivity extends RecyclerViewActivity<PayFeature> implements PayView{

    @Inject PayPresenter presenter;

    private View headerView;
    private List<PayFeature> payFeatures = new ArrayList<>();

    @Override
    public void onInitData(Bundle savedInstanceState) {
        getAppComponent().plus().inject(this);
        presenter.setView(this);
    }

    @Override
    public void onInitView() {
        super.onInitView();
        setCenterTitle("开通会员");
        setLeftImageIcon(R.mipmap.ic_back);
        if (headerView == null){
            headerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_header_pay, null);
            getRecyclerAdapter().addHeaderView(headerView);
        }
    }

    @Override
    public BaseQuickAdapter<PayFeature, BaseViewHolder> getAdapter() {
        return new BaseMultiItemQuickAdapter<PayFeature, BaseViewHolder>(payFeatures){

            {
                addItemType(PayFeature.FEATURETYPE, R.layout.item_pay_feature);
                addItemType(PayFeature.PRODUCTTYPE, R.layout.item_pay_product);
            }

            @Override
            protected void convert(BaseViewHolder helper, PayFeature item) {
                switch (helper.getItemViewType()){
                    case PayFeature.FEATURETYPE:
                        helper.setImageResource(R.id.image_feature_icon, item.featureIcon);
                        helper.setText(R.id.tv_feature_featureName, item.featureName);
                        helper.setText(R.id.tv_feature_featureDescription, item.featureDescription);
                        helper.setVisible(R.id.image_feature_isfree, item.isFree);
                        break;
                    case PayFeature.PRODUCTTYPE:
                        helper.setText(R.id.tv_openPayFeature, item.productName+item.price);
                        helper.addOnClickListener(R.id.tv_openPayFeature);
                        break;
                }
            }
        };
    }

    @Override
    public INetPresenter getPresenter() {
        return presenter;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_payfeatures;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        final PayFeature item = (PayFeature) adapter.getItem(position);
        switch (view.getId()){
            case R.id.tv_openPayFeature:
                BottomSheetHelper.showBottomSheet(this, "选择支付方式", new String[]{"微信", "支付宝"}, new BottomSheetHelper.OnClickListener() {
                    @Override
                    public void onClick(int which) {
                        switch (which){
                            case 0:
                                presenter.payRequest(1, item.productId);
                                break;
                            case 1:
                                presenter.payRequest(2, item.productId);
                                break;
                        }
                    }
                });
                break;
        }
    }

    @Override
    protected void recyclerViewConfig(RecyclerView recyclerView, BaseQuickAdapter adapter) {
        setAutoRefresh(true);
    }

    @Override
    public int getVerticalInterval() {
        return 0;
    }
}
