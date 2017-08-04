package com.king.reading.net.request;

import com.king.reading.ddb.GetBannersRequest;
import com.king.reading.ddb.GetBannersResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 19/07/2017.
 */

public class GetBannersReq extends BaseRequest<GetBannersResponse>{
    public GetBannersReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetBannersRequest request = new GetBannersRequest();
        request.setType((Integer) params[0]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetBannersResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetBannersResponse> getRequestSingle(Api api) {
        return api.getBanners(this);
    }

    @Override
    protected String getRequestName() {
        return GetBannersRequest.class.getSimpleName();
    }
}
