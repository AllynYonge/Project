package com.king.reading.net.request;

import com.king.reading.ddb.UpdateUserInfoRequest;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 04/07/2017.
 */

public class UpdateUserInfoReq extends BaseRequest<Boolean> {
    public UpdateUserInfoReq(Api api, Object... params) {
        super(api,params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object... params) {
        UpdateUserInfoRequest request = new UpdateUserInfoRequest();
        request.setName((String) params[0]);
        request.setNameOfClass((String) params[1]);
        request.setBookId((Long) params[2]);
        request.setSchoolId((Long) params[3]);
        request.setAreaCode((Integer) params[4]);
        request.setAvatar((String) params[5]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(Boolean rsp) {
        return true;
    }

    @Override
    protected Single<Boolean> getRequestSingle(Api api) {
        return api.updateUserInfo(this);
    }

    @Override
    protected String getRequestName() {
        return UpdateUserInfoRequest.class.getSimpleName();
    }
}
