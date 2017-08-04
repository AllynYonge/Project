package com.king.reading.net.request;

import com.king.reading.ddb.RegisterRequest;
import com.king.reading.mod.Header;
import com.king.reading.mod.Response;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 12/07/2017.
 */

public class RegisterReq extends BaseRequest<Response>{
    public RegisterReq(Api api, Object... params) {
            super(api, params);
        }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        RegisterRequest request = new RegisterRequest();
        request.setMobilePhone((String) params[0]);
        request.setPassword((String) params[1]);
        request.setVerifyCode((String) params[2]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(Response rsp) {
        return true;
    }

    @Override
    protected Single<Response> getRequestSingle(Api api) {
        return api.register(this);
    }

    @Override
    protected String getRequestName() {
        return RegisterRequest.class.getSimpleName();
    }

    @Override
    public Header getHeader() {
        Header header = super.getHeader();
        header.token = "";
        header.account = "";
        header.refreshToken = "";
        return header;
    }
}
