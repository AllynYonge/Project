package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by AllynYonge on 19/07/2017.
 */

@Table(database = AppDatabase.class, cachingEnabled = true)
public class WordEntity extends BaseModel{
    @PrimaryKey
    public String id;
    @Column
    public String word;
    @Column
    public String mean;
    @Column
    public String encryptSoundURL;
}
