package com.king.reading.net.request;

import com.king.reading.ddb.ResetPasswordRequest;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 17/07/2017.
 */

public class ResetPasswordReq extends BaseRequest<Boolean>{
    public ResetPasswordReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setMobilePhone((String) params[0]);
        req.setPassword((String) params[1]);
        req.setVerifyCode((String) params[2]);
        req.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(Boolean rsp) {
        return true;
    }

    @Override
    protected Single<Boolean> getRequestSingle(Api api) {
        return api.resetPassword(this);
    }

    @Override
    protected String getRequestName() {
        return ResetPasswordRequest.class.getSimpleName();
    }
}
