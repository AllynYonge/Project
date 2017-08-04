package com.king.reading.net.converter;

import com.google.gson.Gson;
import com.king.reading.common.utils.DebugUtil;
import com.king.reading.common.utils.TarsUtils;
import com.king.reading.encyption.EncryptionManager;
import com.king.reading.exception.BusinessException;
import com.king.reading.mod.Header;
import com.king.reading.mod.Response;
import com.king.reading.mod.Result;
import com.king.reading.net.RequestHeaderHelper;
import com.orhanobut.logger.Logger;
import com.qq.tars.protocol.tars.TarsInputStream;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by hu.yang on 2017/5/5.
 */

public class TarsResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private Type type;
    public TarsResponseBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        byte[] bytes = EncryptionManager.getEncyption().decryption(value.bytes());
        TarsInputStream tarsInputStream = new TarsInputStream(bytes);
        tarsInputStream.setServerEncoding("UTF-8");
        Response response = new Response();
        response.header = new Header();
        response.result = new Result();
        response.readFrom(tarsInputStream);

        if (response.getResult().retCode != 0)
            throw new BusinessException("请求接口：" + response.getHeader().getRequestName() + "错误码：" + response.getResult().retCode, response.getResult().retCode);

        if (type.equals(Response.class)){
            //todo clean
            RequestHeaderHelper.getInstance().replaceHeader(response.getHeader());
            Object bodyRsp = TarsUtils.getBodyRsp(type, response.body);
            Logger.d(((Class) type).getSimpleName() + " : " + new Gson().toJson(bodyRsp));
            return (T) response;
        } else if (type.equals(Boolean.class)){
            RequestHeaderHelper.getInstance().replaceHeader(response.getHeader());
            return (T) Boolean.TRUE;
        } else {
            RequestHeaderHelper.getInstance().replaceHeader(response.getHeader());
            Object bodyRsp = TarsUtils.getBodyRsp(type, response.body);
            Logger.d(((Class) type).getSimpleName() + " : " + new Gson().toJson(bodyRsp));
            return (T) bodyRsp;
        }
    }
}
