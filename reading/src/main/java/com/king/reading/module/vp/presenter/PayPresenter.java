package com.king.reading.module.vp.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.king.reading.R;
import com.king.reading.base.presenter.INetPresenter;
import com.king.reading.base.view.IBaseRecyclerView;
import com.king.reading.common.pay.AliPayReq2;
import com.king.reading.common.pay.PayAPI;
import com.king.reading.common.pay.WechatPayReq;
import com.king.reading.common.utils.ToastUtils;
import com.king.reading.data.entities.ProductEntity;
import com.king.reading.data.repository.OtherRepository;
import com.king.reading.ddb.AliPayInfo;
import com.king.reading.ddb.PlaceOrderResponse;
import com.king.reading.ddb.WXPayInfo;
import com.king.reading.model.PayFeature;
import com.king.reading.module.user.PayFeaturesActivity;
import com.king.reading.module.vp.view.PayView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by AllynYonge on 27/07/2017.
 */

public class PayPresenter implements INetPresenter<PayFeature> {
    private final OtherRepository repository;
    private List<PayFeature> payFeatures;
    private List<PayFeature> products;
    private PayView payView;

    @Inject
    public PayPresenter(OtherRepository repository) {
        this.repository = repository;
        payFeatures = new ArrayList<>();
        products = new ArrayList<>();
    }

    @Override
    public void onRefresh() {
        repository.getProducts().subscribe(new Consumer<List<ProductEntity>>() {
            @Override
            public void accept(@NonNull List<ProductEntity> productEntities) throws Exception {
                payFeatures.removeAll(products);
                products.clear();
                for (ProductEntity entity : productEntities) {
                    products.add(new PayFeature(entity.price, entity.name, entity.productID, PayFeature.PRODUCTTYPE));
                }
                payFeatures.addAll(products);
                payView.refreshUI(payFeatures);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                ToastUtils.show(throwable.getMessage());
            }
        });
    }

    @Override
    public void onLoadMore(IBaseRecyclerView<PayFeature> netView) {

    }


    public void setView(PayView payView){
        this.payView = payView;
        payFeatures.add(new PayFeature(R.mipmap.ic_click_read, "点读", "课本点读,帮助课前预习、课后复习", true, 0));
        payFeatures.add(new PayFeature(R.mipmap.ic_follow_read, "跟读", "智能语音评测，纠正语句发音", false, 0));
        payFeatures.add(new PayFeature(R.mipmap.ic_role_play, "角色扮演", "模拟课堂对话，分角色扮演", false, 0));
        payFeatures.add(new PayFeature(R.mipmap.ic_random_listen, "同步听", "同步教材磁带，随时随地练习听力", false, 0));
        payFeatures.add(new PayFeature(R.mipmap.ic_listen_write, "单词听写", "模拟老师报写单词，帮助记忆", false, 0));
    }

    public void payRequest(final int payType, int productId) {
        repository.getPayInfo(payType, productId).subscribe(new Consumer<PlaceOrderResponse>() {
            @Override
            public void accept(@NonNull PlaceOrderResponse placeOrderResponse) throws Exception {
                if (placeOrderResponse.payType == 2){
                    payAli(placeOrderResponse.aliPayInfo);
                } else if (placeOrderResponse.payType == 1){
                    payWeiXin(placeOrderResponse.wxPayInfo);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                ToastUtils.show(throwable.getMessage());
            }
        });
    }

    private void payAli(AliPayInfo aliPayInfo){
        AliPayReq2 aliPayReq2 = new AliPayReq2.Builder().with((Activity) payView).setSignedAliPayOrderInfo(aliPayInfo.payInfo).create();
        aliPayReq2.setOnAliPayListener(new AliPayReq2.OnAliPayListener() {
            @Override
            public void onPaySuccess(String resultInfo) {
                Logger.d(resultInfo);
            }

            @Override
            public void onPayFailure(String resultInfo) {
                Logger.d(resultInfo);
            }

            @Override
            public void onPayConfirmimg(String resultInfo) {
                Logger.d(resultInfo);
            }

            @Override
            public void onPayCheck(String status) {
                Logger.d(status);
            }
        });
        PayAPI.getInstance().sendPayRequest(aliPayReq2);
    }

    private void payWeiXin(WXPayInfo wxPayInfo){
        WechatPayReq wechatPayReq = new WechatPayReq.Builder()
                .with(PayFeaturesActivity.class.cast(payView)) //activity instance
                .setAppId(wxPayInfo.appid) //wechat pay AppID
                .setPartnerId(wxPayInfo.partnerid)//wechat pay partner id
                .setPrepayId(wxPayInfo.prepayid)//pre pay id
                .setNonceStr(wxPayInfo.noncestr)
                .setTimeStamp(wxPayInfo.timestamp)//time stamp
                .setSign(wxPayInfo.sign)//sign
                .create();
        PayAPI.getInstance().sendPayRequest(wechatPayReq);
    }
}
