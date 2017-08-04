package com.king.reading.model;

import android.support.annotation.DrawableRes;

import com.king.reading.common.adapter.entity.MultiItemEntity;

/**
 * Created by hu.yang on 2017/6/19.
 */

public class PayFeature implements MultiItemEntity{
    public static final int FEATURETYPE = 0;
    public static final int PRODUCTTYPE = 1;
    public int productId;
    public @DrawableRes int featureIcon;
    public String featureName;
    public String featureDescription;
    public boolean isFree;
    public int type;

    public int price;
    public String productName;

    public PayFeature(int featureIcon, String featureName, String featureDescription, boolean isFree, int type) {
        this.featureIcon = featureIcon;
        this.featureName = featureName;
        this.featureDescription = featureDescription;
        this.isFree = isFree;
        this.type = type;
    }

    public PayFeature(int price, String productName, int productId, int type) {
        this.price = price;
        this.productName = productName;
        this.productId = productId;
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public int hashCode() {
        if (featureIcon != 0){
            return featureIcon;
        } else if (productId != 0){
            return productId;
        }
        return 0;
    }
}
