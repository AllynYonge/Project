package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.ColumnIgnore;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by AllynYonge on 06/07/2017.
 */

@Table(database = AppDatabase.class, allFields = true)
public class ModuleEntity extends BaseModel{
    @PrimaryKey
    public String id;
    public long moduleId;
    public int resourceId;
    public boolean isWord;
    public String name;
    public String title;
    public int start;
    public int end;

    @ColumnIgnore
    public List<UnitEntity> units;

    @OneToMany(methods = OneToMany.Method.ALL, variableName = "units")
    public List<UnitEntity> getUnits(){
        if (units == null || units.isEmpty()){
            units = SQLite.select().from(UnitEntity.class).where(UnitEntity_Table.moduleId.eq(id)).queryList();
        }
        return units;
    }
}
