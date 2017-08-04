package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by AllynYonge on 06/07/2017.
 */

@Table(database = AppDatabase.class, allFields = true)
public class UnitEntity extends BaseModel {
    @PrimaryKey
    public String id;
    public long unitId;
    public String moduleId;
    public String name;
    public String title;
    public String coverURL;
    public boolean canRolePlay;
    public boolean isWord;
    public int start;
    public int end;
    public int resourceId;
}
