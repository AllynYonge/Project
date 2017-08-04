package com.king.reading.net.request;

import com.king.reading.ddb.PlaceOrderRequest;
import com.king.reading.ddb.PlaceOrderResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 27/07/2017.
 */

public class PlaceOrderReq extends BaseRequest<PlaceOrderResponse>{
    public PlaceOrderReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setPayType((Integer) params[0]);
        request.setProductId((Integer) params[1]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(PlaceOrderResponse rsp) {
        return true;
    }

    @Override
    protected Single<PlaceOrderResponse> getRequestSingle(Api api) {
        return api.getPayInfo(this);
    }

    @Override
    protected String getRequestName() {
        return PlaceOrderRequest.class.getSimpleName();
    }
}
