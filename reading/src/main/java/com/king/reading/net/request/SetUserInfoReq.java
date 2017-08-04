package com.king.reading.net.request;

import com.king.reading.ddb.SetUserInfoRequest;
import com.king.reading.ddb.SetUserInfoResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 07/07/2017.
 */

public class SetUserInfoReq extends BaseRequest<SetUserInfoResponse>{
    public SetUserInfoReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        SetUserInfoRequest request = new SetUserInfoRequest();
        request.setType((Integer) params[0]);
        request.setValue((String) params[1]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(SetUserInfoResponse rsp) {
        return true;
    }

    @Override
    protected Single<SetUserInfoResponse> getRequestSingle(Api api) {
        return null;
    }

    @Override
    protected String getRequestName() {
        return SetUserInfoRequest.class.getSimpleName();
    }
}
