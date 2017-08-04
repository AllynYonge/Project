package com.king.reading.net.request;

import com.king.reading.ddb.GetBookRequest;
import com.king.reading.ddb.GetBookResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 06/07/2017.
 */

public class GetBookReq extends BaseRequest<GetBookResponse>{

    public GetBookReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetBookRequest request = new GetBookRequest();
        request.bookID = (long) params[0];
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetBookResponse rsp) {
        return rsp.book != null && rsp.book.getBase().bookID != 0;
    }

    @Override
    protected Single<GetBookResponse> getRequestSingle(Api api) {
        return api.getBookDetail(this);
    }

    @Override
    protected String getRequestName() {
        return GetBookRequest.class.getSimpleName();
    }
}
