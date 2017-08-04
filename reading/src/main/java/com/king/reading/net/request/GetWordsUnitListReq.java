package com.king.reading.net.request;

import com.blankj.utilcode.util.EmptyUtils;
import com.king.reading.ddb.GetWordsUnitListRequest;
import com.king.reading.ddb.GetWordsUnitListResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by hu.yang on 2017/8/4.
 */

public class GetWordsUnitListReq extends BaseRequest<GetWordsUnitListResponse> {
    public GetWordsUnitListReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetWordsUnitListRequest request = new GetWordsUnitListRequest();
        request.setBookID((Long) params[0]);
        request.setResourceID((Integer) params[1]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetWordsUnitListResponse rsp) {
        return EmptyUtils.isNotEmpty(rsp.getUnits());
    }

    @Override
    protected Single<GetWordsUnitListResponse> getRequestSingle(Api api) {
        return api.WordUnitList(this);
    }

    @Override
    protected String getRequestName() {
        return GetWordsUnitListRequest.class.getSimpleName();
    }
}
