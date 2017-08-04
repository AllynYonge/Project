package com.king.reading.net.request;

import com.king.reading.ddb.GetPageRequest;
import com.king.reading.ddb.GetPageResponse;
import com.king.reading.ddb.PageRange;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 03/07/2017.
 */

public class GetPageReq extends BaseRequest<GetPageResponse>{
    public GetPageReq(Api api, Object... params) {
        super(api, params);
    }


    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetPageRequest getPageRequest = new GetPageRequest();
        getPageRequest.setResourceID((int) params[0]);
        getPageRequest.setPageRange(new PageRange((int)params[1], (int)params[2]));
        getPageRequest.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetPageResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetPageResponse> getRequestSingle(Api api) {
        return api.getPages(this);
    }

    @Override
    protected String getRequestName() {
        return GetPageRequest.class.getSimpleName();
    }
}
