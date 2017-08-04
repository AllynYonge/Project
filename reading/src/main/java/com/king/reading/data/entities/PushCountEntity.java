package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.king.reading.ddb.GetPushCountResponse;
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
public class PushCountEntity extends BaseModel {
    @PrimaryKey
    public long userId;

    @Column(typeConverter = PushCountConverter.class)
    public GetPushCountResponse pushCountRsp;

    @com.raizlabs.android.dbflow.annotation.TypeConverter
    public static class PushCountConverter extends TypeConverter<byte[], GetPushCountResponse> {

        @Override
        public byte[] getDBValue(GetPushCountResponse model) {
            TarsOutputStream stream = new TarsOutputStream();
            model.writeTo(stream);
            return stream.toByteArray();
        }

        @Override
        public GetPushCountResponse getModelValue(byte[] jsonData) {
            TarsInputStream stream = new TarsInputStream(jsonData);
            GetPushCountResponse rsp = new GetPushCountResponse();
            rsp.readFrom(stream);
            return rsp;
        }

    }
}
