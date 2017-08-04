package com.king.reading.net.request;

import com.king.reading.ddb.ChangePasswordRequest;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 17/07/2017.
 */

public class ChangePasswordReq extends BaseRequest<Boolean> {
    public ChangePasswordReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword((String) params[0]);
        request.setNewPassword((String) params[1]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(Boolean rsp) {
        return true;
    }

    @Override
    protected Single<Boolean> getRequestSingle(Api api) {
        return api.changePassword(this);
    }

    @Override
    protected String getRequestName() {
        return ChangePasswordRequest.class.getSimpleName();
    }
}
