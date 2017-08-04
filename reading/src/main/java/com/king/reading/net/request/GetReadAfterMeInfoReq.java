package com.king.reading.net.request;

import com.king.reading.ddb.GetReadAfterMeInfoRequest;
import com.king.reading.ddb.GetReadAfterMeInfoResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 31/07/2017.
 */

public class GetReadAfterMeInfoReq extends BaseRequest<GetReadAfterMeInfoResponse> {
    public GetReadAfterMeInfoReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetReadAfterMeInfoRequest request = new GetReadAfterMeInfoRequest();
        request.setBookID((Long) params[0]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetReadAfterMeInfoResponse rsp) {
        return rsp != null;
    }

    @Override
    protected Single<GetReadAfterMeInfoResponse> getRequestSingle(Api api) {
        return api.getReadAfterMeInfo(this);
    }

    @Override
    protected String getRequestName() {
        return GetReadAfterMeInfoRequest.class.getSimpleName();
    }
}
