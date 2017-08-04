package com.king.reading.net.request;

import com.king.reading.ddb.LoginRequest;
import com.king.reading.mod.Header;
import com.king.reading.mod.Response;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by hu.yang on 2017/5/8.
 */

public class LoginReq extends BaseRequest<Response> {
    public LoginReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object... params) {
        LoginRequest loginReq = new LoginRequest();
        loginReq.setUsername((String) params[0]);
        loginReq.setPassword((String) params[1]);
        loginReq.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(Response rsp) {
        return true/*rsp.getAccountId() != 0*/;
    }

    @Override
    protected Single<Response> getRequestSingle(Api api) {
        return api.LoginReq(this);
    }

    @Override
    protected String getRequestName() {
        return LoginRequest.class.getSimpleName();
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
