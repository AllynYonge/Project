package com.king.reading.net.request;

import com.king.reading.ddb.GetNotificationsRequest;
import com.king.reading.ddb.GetNotificationsResponse;
import com.king.reading.ddb.PageContext;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 28/07/2017.
 */

public class GetNotificationsReq extends BaseRequest<GetNotificationsResponse> {
    public GetNotificationsReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetNotificationsRequest request = new GetNotificationsRequest();
        request.setPageContext((PageContext) params[0]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetNotificationsResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetNotificationsResponse> getRequestSingle(Api api) {
        return api.getNotifications(this);
    }

    @Override
    protected String getRequestName() {
        return GetNotificationsRequest.class.getSimpleName();
    }
}
