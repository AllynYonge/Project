package com.king.reading.net.request;

import com.king.reading.ddb.GetUnitWordsRequest;
import com.king.reading.ddb.GetUnitWordsResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 19/07/2017.
 */

public class GetUnitWordsReq extends BaseRequest<GetUnitWordsResponse> {
    public GetUnitWordsReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetUnitWordsRequest request = new GetUnitWordsRequest();
        request.setResourceID((Integer) params[0]);
        request.setUnitID((Long) params[1]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetUnitWordsResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetUnitWordsResponse> getRequestSingle(Api api) {
        return api.getUnitWords(this);
    }

    @Override
    protected String getRequestName() {
        return GetUnitWordsRequest.class.getSimpleName();
    }
}
