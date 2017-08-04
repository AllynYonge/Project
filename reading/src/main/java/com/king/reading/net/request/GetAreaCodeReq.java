package com.king.reading.net.request;

import com.blankj.utilcode.util.EmptyUtils;
import com.king.reading.ddb.GetAreaCodeRequest;
import com.king.reading.ddb.GetAreaCodeResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 03/07/2017.
 */

public class GetAreaCodeReq extends BaseRequest<GetAreaCodeResponse>{
    public GetAreaCodeReq(Api api, Object... params) {
        super(api, params);
    }


    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetAreaCodeRequest request = new GetAreaCodeRequest();
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetAreaCodeResponse rsp) {
        return EmptyUtils.isNotEmpty(rsp.getProvinces());
    }

    @Override
    protected Single<GetAreaCodeResponse> getRequestSingle(Api api) {
        return api.getArea(this);
    }

    @Override
    protected String getRequestName() {
        return GetAreaCodeRequest.class.getSimpleName();
    }
}
