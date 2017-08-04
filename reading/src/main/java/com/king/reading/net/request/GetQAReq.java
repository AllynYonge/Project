package com.king.reading.net.request;


import com.blankj.utilcode.util.EmptyUtils;
import com.king.reading.ddb.GetQARequest;
import com.king.reading.ddb.GetQAResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 31/07/2017.
 */

public class GetQAReq extends BaseRequest<GetQAResponse> {

    public GetQAReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetQARequest request = new GetQARequest();
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetQAResponse rsp) {
        return EmptyUtils.isNotEmpty(rsp);
    }

    @Override
    protected Single<GetQAResponse> getRequestSingle(Api api) {
        return api.getQA(this);
    }

    @Override
    protected String getRequestName() {
        return GetQARequest.class.getSimpleName();
    }
}
