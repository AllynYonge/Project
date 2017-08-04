package com.king.reading.net.request;

import com.king.reading.ddb.GetPlayBookRequest;
import com.king.reading.ddb.GetPlayBookResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 19/07/2017.
 */

public class GetPlayBookReq extends BaseRequest<GetPlayBookResponse> {
    public GetPlayBookReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetPlayBookRequest request = new GetPlayBookRequest();
        request.setResourceID((Integer) params[0]);
        request.setUnitID((Long) params[1]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetPlayBookResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetPlayBookResponse> getRequestSingle(Api api) {
        return api.getPlayBook(this);
    }

    @Override
    protected String getRequestName() {
        return GetPlayBookRequest.class.getSimpleName();
    }
}
