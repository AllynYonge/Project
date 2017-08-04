package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.king.reading.ddb.ReadAfterMe;
import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.converter.TypeConverter;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by AllynYonge on 31/07/2017.
 */

@Table(database = AppDatabase.class)
public class ReadAfterMeEntity extends BaseModel {
    @PrimaryKey
    public long userId;
    @Column(typeConverter = ReadAfterMeTypeConverter.class)
    public ReadAfterMe readAfterMe;

    public static class ReadAfterMeTypeConverter extends TypeConverter<byte[], ReadAfterMe>{
        @Override
        public byte[] getDBValue(ReadAfterMe model) {
            TarsOutputStream stream = new TarsOutputStream();
            model.writeTo(stream);
            return stream.toByteArray();
        }

        @Override
        public ReadAfterMe getModelValue(byte[] data) {
            TarsInputStream stream = new TarsInputStream(data);
            ReadAfterMe readAfterMe = new ReadAfterMe();
            readAfterMe.readFrom(stream);
            return readAfterMe;
        }
    }
}
