package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by AllynYonge on 29/07/2017.
 */

@Table(database = AppDatabase.class, allFields = true, cachingEnabled = true)
public class ActivityEntity extends BaseModel {
    @PrimaryKey
    public int activityId;
    public String detail; //详情
    public String coverURL; //活动封页
    public long startTime; //活动开始时间
    public long endTime; //活动结束时间
    public String activityUrl;
    public String activityDataUrl;
    public String iconUrl;

}
