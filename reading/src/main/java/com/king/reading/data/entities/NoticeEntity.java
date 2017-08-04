package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by AllynYonge on 29/07/2017.
 */

@Table(database = AppDatabase.class, allFields = true, cachingEnabled = true)
public class NoticeEntity extends BaseModel {
    @PrimaryKey
    public int id;

    public String title;
    public String content;
    public long createTime;
    public String jumpURL;
    public long userId;
}
