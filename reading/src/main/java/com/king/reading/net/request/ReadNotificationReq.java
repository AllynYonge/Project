package com.king.reading.net.request;

import com.king.reading.ddb.ReadNotificationRequest;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 31/07/2017.
 */

public class ReadNotificationReq extends BaseRequest<Boolean> {
    public ReadNotificationReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        ReadNotificationRequest request = new ReadNotificationRequest();
        request.setNotificationId((Integer) params[0]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(Boolean rsp) {
        return true;
    }

    @Override
    protected Single<Boolean> getRequestSingle(Api api) {
        return api.noticeMarkRead(this);
    }

    @Override
    protected String getRequestName() {
        return ReadNotificationRequest.class.getSimpleName();
    }
}
