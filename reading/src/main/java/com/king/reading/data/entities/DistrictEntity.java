package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.king.reading.widget.pickerview.model.IPickerViewData;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by AllynYonge on 13/07/2017.
 */

@Table(database = AppDatabase.class)
public class DistrictEntity extends BaseModel implements IPickerViewData {
    @PrimaryKey
    public int areaCode;
    @Column
    public String name;
    @Column
    public String cityName;

    @Override
    public String getPickerViewText() {
        return name;
    }
}
