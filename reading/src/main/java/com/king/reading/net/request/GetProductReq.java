package com.king.reading.net.request;

import com.king.reading.ddb.GetProductRequest;
import com.king.reading.ddb.GetProductResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 27/07/2017.
 */

public class GetProductReq extends BaseRequest<GetProductResponse> {
    public GetProductReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetProductRequest request = new GetProductRequest();
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetProductResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetProductResponse> getRequestSingle(Api api) {
        return api.getProduct(this);
    }

    @Override
    protected String getRequestName() {
        return GetProductRequest.class.getSimpleName();
    }
}
