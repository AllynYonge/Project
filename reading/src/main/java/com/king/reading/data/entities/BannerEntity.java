package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by AllynYonge on 20/07/2017.
 */

@Table(database = AppDatabase.class)
public class BannerEntity extends BaseModel{
    @PrimaryKey(autoincrement = true)
    public int id;
    @Column
    public String url;
    @Column
    public String bannerUrl;
    @Column
    public int type;
}
