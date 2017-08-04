package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by AllynYonge on 13/07/2017.
 */

@Table(database = AppDatabase.class)
public class SchoolEntity {
    @PrimaryKey
    public long schoolId;
    @Column
    public String name;
    @Column
    public int districtCode;

    public SchoolEntity() {
    }

    public SchoolEntity(long schoolId, String name, int districtCode) {
        this.schoolId = schoolId;
        this.name = name;
        this.districtCode = districtCode;
    }
}
