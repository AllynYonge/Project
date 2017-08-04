package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by AllynYonge on 31/07/2017.
 */

@Table(database = AppDatabase.class)
public class CourseEntity extends BaseModel {
    @PrimaryKey
    public long courseId;
    @Column
    public String name; //课程名称
    @Column
    public String iconURL; //图标地址,对加密的资源名进行了标识
    @Column
    public String url; //课程地址
}
