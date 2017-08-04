package com.king.reading.data.entities;

import com.king.reading.common.utils.EncryptUtil;
import com.king.reading.data.db.AppDatabase;
import com.king.reading.ddb.Page;
import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.converter.TypeConverter;
import com.raizlabs.android.dbflow.structure.BaseModel;

import static com.king.reading.C.SECRETKEY;

/**
 * Created by AllynYonge on 07/07/2017.
 */

@Table(database = AppDatabase.class, cachingEnabled = true)
public class PageEntity extends BaseModel{
    @PrimaryKey
    public int id;
    @Column
    public int number;
    @Column(typeConverter = PageConverter.class)
    public Page page;

    @com.raizlabs.android.dbflow.annotation.TypeConverter
    public static class PageConverter extends TypeConverter<byte[], Page> {

        @Override
        public byte[] getDBValue(Page model) {
            TarsOutputStream stream = new TarsOutputStream();
            model.writeTo(stream);
            return EncryptUtil.desEncrypt(SECRETKEY,stream.toByteArray());
        }

        @Override
        public Page getModelValue(byte[] jsonData) {
            TarsInputStream stream = new TarsInputStream(EncryptUtil.desDecrypt(SECRETKEY,jsonData));
            stream.setServerEncoding("UTF-8");
            Page page = new Page();
            page.readFrom(stream);
            return page;
        }

    }
}
