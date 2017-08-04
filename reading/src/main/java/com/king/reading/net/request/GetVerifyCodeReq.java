package com.king.reading.net.request;

import com.king.reading.ddb.GetVerifyCodeRequest;
import com.king.reading.ddb.GetVerifyCodeResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 12/07/2017.
 */

public class GetVerifyCodeReq extends BaseRequest<GetVerifyCodeResponse>{
    public GetVerifyCodeReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetVerifyCodeRequest request = new GetVerifyCodeRequest();
        request.setMobilePhone((String) params[0]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetVerifyCodeResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetVerifyCodeResponse> getRequestSingle(Api api) {
        return api.getVerifyCode(this);
    }

    @Override
    protected String getRequestName() {
        return GetVerifyCodeRequest.class.getSimpleName();
    }
}
