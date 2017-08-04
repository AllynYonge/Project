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

@Table(database = AppDatabase.class, allFields = true, cachingEnabled = true)
public class BookEntity extends BaseModel{
    @PrimaryKey
    public int resourceId;
    public long bookId;
    public int startPage;
    public int endPage;

    @ColumnIgnore
    public List<ModuleEntity> modules;

    @OneToMany(methods = OneToMany.Method.ALL, variableName = "modules")
    public List<ModuleEntity> getModules(){
        if (modules == null || modules.isEmpty()){
            modules = SQLite.select().from(ModuleEntity.class).where(ModuleEntity_Table.resourceId.eq(resourceId)).queryList();
        }
        return modules;
    }
}
