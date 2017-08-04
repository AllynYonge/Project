package com.king.reading.data.repository;

import com.king.reading.common.utils.ACacheMgr;
import com.king.reading.data.entities.ActivityEntity;
import com.king.reading.data.entities.BannerEntity;
import com.king.reading.data.entities.BannerEntity_Table;
import com.king.reading.data.entities.DbMappers;
import com.king.reading.data.entities.ProductEntity;
import com.king.reading.ddb.GetActivitiesResponse;
import com.king.reading.ddb.GetBannersResponse;
import com.king.reading.ddb.GetProductResponse;
import com.king.reading.ddb.PlaceOrderResponse;
import com.king.reading.net.Api;
import com.king.reading.net.request.GetActivitiesReq;
import com.king.reading.net.request.GetBannersReq;
import com.king.reading.net.request.GetProductReq;
import com.king.reading.net.request.PlaceOrderReq;
import com.king.reading.net.request.SendFeedbackReq;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by AllynYonge on 20/07/2017.
 */

@Singleton
public class OtherRepository {

    private Api api;
    private Api testApi;

    @Inject
    public OtherRepository(@Named("tars") Api api, @Named("gson") Api testApi) {
        this.api = api;
        this.testApi = testApi;
    }

    //获取Banner列表
    public Flowable<List<BannerEntity>> getBanner(final int bannerType){
        GetBannersReq req = new GetBannersReq(api, bannerType);
        return req.sendRequest().map(new Function<GetBannersResponse, List<BannerEntity>>() {
            @Override
            public List<BannerEntity> apply(@NonNull GetBannersResponse getBannersResponse) throws Exception {
                return DbMappers.mapperBanners(bannerType, getBannersResponse.banners);
            }
        }).doOnSuccess(new Consumer<List<BannerEntity>>() {
            @Override
            public void accept(@NonNull List<BannerEntity> bannerEntities) throws Exception {
                List<BannerEntity> old = SQLite.select(BannerEntity_Table.id).from(BannerEntity.class).queryList();
                FlowManager.getModelAdapter(BannerEntity.class).deleteAll(old);
                FlowManager.getModelAdapter(BannerEntity.class).saveAll(bannerEntities);
            }
        }).mergeWith(new SingleSource<List<BannerEntity>>() {
            @Override
            public void subscribe(@NonNull SingleObserver<? super List<BannerEntity>> observer) {
                List<BannerEntity> bannerEntities = SQLite.select(BannerEntity_Table.id).from(BannerEntity.class).queryList();
                bannerEntities = bannerEntities.isEmpty() ? new ArrayList<BannerEntity>() : bannerEntities;
                observer.onSuccess(bannerEntities);
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }


    public Flowable<List<ProductEntity>> getProducts(){
        GetProductReq req = new GetProductReq(api);

        return req.sendRequest().map(new Function<GetProductResponse, List<ProductEntity>>() {
            @Override
            public List<ProductEntity> apply(@NonNull GetProductResponse getProductResponse) throws Exception {
                SQLite.delete(ProductEntity.class).execute();
                List<ProductEntity> newEntities = DbMappers.mapperProduct(getProductResponse);
                FlowManager.getModelAdapter(ProductEntity.class).saveAll(newEntities);
                return newEntities;
            }
        }).mergeWith(RXSQLite.rx(SQLite.select().from(ProductEntity.class)).queryList())
        .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<PlaceOrderResponse> getPayInfo(int payType, int orderId){
        PlaceOrderReq req = new PlaceOrderReq(api, payType, orderId);
        return req.sendRequest();
    }

    public Flowable<List<ActivityEntity>> getActivities(final boolean isPullRefresh){
        GetActivitiesReq req = new GetActivitiesReq(api, isPullRefresh ? null : ACacheMgr.getActivitiesPageContext());
        return Single.merge(req.sendRequest().map(new Function<GetActivitiesResponse, List<ActivityEntity>>() {
            @Override
            public List<ActivityEntity> apply(@NonNull GetActivitiesResponse getActivitiesResponse) throws Exception {
                return DbMappers.mapperActivities(getActivitiesResponse);
            }
        }).doOnSuccess(new Consumer<List<ActivityEntity>>() {
            @Override
            public void accept(@NonNull List<ActivityEntity> noticeEntities) throws Exception {
                if (isPullRefresh){
                    SQLite.delete(ActivityEntity.class).execute();
                    FlowManager.getModelAdapter(ActivityEntity.class).saveAll(noticeEntities);
                }
            }
        }),RXSQLite.rx(SQLite.select().from(ActivityEntity.class)).queryList())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发送通知反馈
     */
    public Single<Boolean> SendFeedBack(String content){
        SendFeedbackReq req = new SendFeedbackReq(api, content);
        return req.sendRequest();
    }
}
