package com.king.reading.net.request;

import com.king.reading.ddb.GetSchoolsRequest;
import com.king.reading.ddb.GetSchoolsResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 13/07/2017.
 */

public class GetSchoolReq extends BaseRequest<GetSchoolsResponse>{
    public GetSchoolReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetSchoolsRequest request = new GetSchoolsRequest();
        request.setAreaCode((int) params[0]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetSchoolsResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetSchoolsResponse> getRequestSingle(Api api) {
        return api.getSchoolList(this);
    }

    @Override
    protected String getRequestName() {
        return GetSchoolsRequest.class.getSimpleName();
    }
}
