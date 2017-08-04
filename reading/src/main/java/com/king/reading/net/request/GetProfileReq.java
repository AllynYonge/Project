package com.king.reading.net.request;

import com.king.reading.ddb.ProfileRequest;
import com.king.reading.mod.Response;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 31/07/2017.
 */

public class GetProfileReq extends BaseRequest<Response> {
    public GetProfileReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        ProfileRequest request = new ProfileRequest();
        request.setUserId((Long) params[0]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(Response rsp) {
        return true;
    }

    @Override
    protected Single<Response> getRequestSingle(Api api) {
        return api.getProfile(this);
    }

    @Override
    protected String getRequestName() {
        return ProfileRequest.class.getSimpleName();
    }
}
