package com.king.reading.net.request;

import com.king.reading.ddb.GetActivitiesRequest;
import com.king.reading.ddb.GetActivitiesResponse;
import com.king.reading.ddb.PageContext;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 28/07/2017.
 */

public class GetActivitiesReq extends BaseRequest<GetActivitiesResponse>{
    public GetActivitiesReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetActivitiesRequest request = new GetActivitiesRequest();
        request.setPageContext((PageContext) params[0]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetActivitiesResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetActivitiesResponse> getRequestSingle(Api api) {
        return api.getActivities(this);
    }

    @Override
    protected String getRequestName() {
        return GetActivitiesRequest.class.getSimpleName();
    }
}
