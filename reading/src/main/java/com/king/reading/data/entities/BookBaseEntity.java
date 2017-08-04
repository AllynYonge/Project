package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by AllynYonge on 03/08/2017.
 */

@Table(database = AppDatabase.class, cachingEnabled = true)
public class BookBaseEntity extends BaseModel{
    @PrimaryKey
    public long bookId;
    @Column
    public String bookName;
    @Column
    public String coverURL;
    @Column
    public String fullName;
    @Column
    public String areaName;
    @Column
    public int resourceId;
}
