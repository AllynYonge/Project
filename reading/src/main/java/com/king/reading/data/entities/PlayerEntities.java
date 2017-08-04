package com.king.reading.data.entities;

import com.king.reading.data.db.AppDatabase;
import com.king.reading.ddb.GetReadAfterMeGameBoardResponse;
import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.converter.TypeConverter;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by AllynYonge on 01/08/2017.
 */

@Table(database = AppDatabase.class)
public class PlayerEntities extends BaseModel{
    @PrimaryKey
    public long userId;

    @Column(typeConverter = GameBoardTypeConverter.class)
    public GetReadAfterMeGameBoardResponse gameBoard;

    public static class GameBoardTypeConverter extends TypeConverter<byte[], GetReadAfterMeGameBoardResponse>{

        @Override
        public byte[] getDBValue(GetReadAfterMeGameBoardResponse model) {
            TarsOutputStream stream = new TarsOutputStream();
            model.writeTo(stream);
            return stream.toByteArray();
        }

        @Override
        public GetReadAfterMeGameBoardResponse getModelValue(byte[] data) {
            TarsInputStream stream = new TarsInputStream(data);
            stream.setServerEncoding("UTF-8");
            GetReadAfterMeGameBoardResponse response = new GetReadAfterMeGameBoardResponse();
            response.readFrom(stream);
            return response;
        }
    }
}
