package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by AllynYonge on 27/07/2017.
 */

@Table(database = AppDatabase.class, allFields = true)
public class ProductEntity extends BaseModel{
    @PrimaryKey
    public int productID; //商品ID
    public String name; //商品名称
    public int price; //商品价格
    public int type; //商品类型
}
