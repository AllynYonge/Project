package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by AllynYonge on 13/07/2017.
 */

@Table(database = AppDatabase.class)
public class CityEntity extends BaseModel{
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String name;

    @Column
    public String provinceName;

    public List<DistrictEntity> districts;

    @OneToMany(variableName = "districts", methods = OneToMany.Method.ALL)
    public List<DistrictEntity> getDistricts(){
        if (districts == null || districts.isEmpty()){
            districts = SQLite.select().from(DistrictEntity.class).where(DistrictEntity_Table.cityName.eq(name)).queryList();
        }
        return districts;
    }
}
