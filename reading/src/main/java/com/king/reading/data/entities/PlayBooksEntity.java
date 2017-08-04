package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.king.reading.ddb.GetPlayBookResponse;
import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.converter.TypeConverter;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by AllynYonge on 19/07/2017.
 */

@Table(database = AppDatabase.class, cachingEnabled = true)
public class PlayBooksEntity extends BaseModel {
    @PrimaryKey
    public String id;

    @Column(typeConverter = PlayBooksConverter.class)
    public GetPlayBookResponse playBooks;

    @com.raizlabs.android.dbflow.annotation.TypeConverter
    public static class PlayBooksConverter extends TypeConverter<byte[], GetPlayBookResponse> {

        @Override
        public byte[] getDBValue(GetPlayBookResponse model) {
            TarsOutputStream stream = new TarsOutputStream();
            model.writeTo(stream);
            return stream.toByteArray();
        }

        @Override
        public GetPlayBookResponse getModelValue(byte[] data) {
            TarsInputStream stream = new TarsInputStream(data);
            stream.setServerEncoding("UTF-8");
            GetPlayBookResponse response = new GetPlayBookResponse();
            response.readFrom(stream);
            return response;
        }
    }
}
