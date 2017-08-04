package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by AllynYonge on 06/07/2017.
 */

@Table(database = AppDatabase.class, cachingEnabled = true)
public class ProvinceEntity extends BaseModel{
    @PrimaryKey
    public String name;

    public List<CityEntity> citys;

    @OneToMany(variableName = "citys", methods = OneToMany.Method.ALL)
    public List<CityEntity> getCitys(){
        if (citys == null || citys.isEmpty()){
            citys = SQLite.select().from(CityEntity.class).where(CityEntity_Table.provinceName.eq(name)).queryList();
        }
        return citys;
    }
    @Override
    public String toString() {
        return "ProvinceEntity{" +
                "name='" + name + '\'' +
                ", citys=" + citys +
                '}';
    }
}
