package com.king.reading.net.request;

import com.king.reading.ddb.CleanPushCountRequest;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 31/07/2017.
 */

public class CleanPushCountReq extends BaseRequest<Boolean> {
    public CleanPushCountReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        CleanPushCountRequest request = new CleanPushCountRequest();
        request.setPushKey((String) params[0]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(Boolean rsp) {
        return rsp;
    }

    @Override
    protected Single<Boolean> getRequestSingle(Api api) {
        return api.cleanPushCount(this);
    }

    @Override
    protected String getRequestName() {
        return CleanPushCountRequest.class.getSimpleName();
    }
}
