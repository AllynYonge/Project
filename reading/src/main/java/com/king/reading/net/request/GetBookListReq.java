package com.king.reading.net.request;

import com.king.reading.ddb.GetBooklistRequest;
import com.king.reading.ddb.GetBooklistResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 06/07/2017.
 */

public class GetBookListReq extends BaseRequest<GetBooklistResponse>{

    public GetBookListReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetBooklistRequest request = new GetBooklistRequest();
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetBooklistResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetBooklistResponse> getRequestSingle(Api api) {
        return api.getBooks(this);
    }

    @Override
    protected String getRequestName() {
        return GetBooklistRequest.class.getSimpleName();
    }
}
