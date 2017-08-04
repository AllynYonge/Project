package com.king.reading.net.request;

import com.king.reading.ddb.GetSecKeyRequest;
import com.king.reading.ddb.GetSecKeyResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 17/07/2017.
 */

public class GetSecKeyReq extends BaseRequest<GetSecKeyResponse>{
    public GetSecKeyReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetSecKeyRequest request = new GetSecKeyRequest();
        request.setResourceID((Integer) params[0]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetSecKeyResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetSecKeyResponse> getRequestSingle(Api api) {
        return api.getSecKey(this);
    }

    @Override
    protected String getRequestName() {
        return GetSecKeyRequest.class.getSimpleName();
    }
}
