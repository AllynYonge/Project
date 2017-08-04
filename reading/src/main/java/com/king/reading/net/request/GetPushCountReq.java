package com.king.reading.net.request;

import com.king.reading.ddb.GetPushCountRequest;
import com.king.reading.ddb.GetPushCountResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 31/07/2017.
 */

public class GetPushCountReq extends BaseRequest<GetPushCountResponse> {
    public GetPushCountReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetPushCountRequest request = new GetPushCountRequest();
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetPushCountResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetPushCountResponse> getRequestSingle(Api api) {
        return api.getPushCount(this);
    }

    @Override
    protected String getRequestName() {
        return GetPushCountRequest.class.getSimpleName();
    }
}
