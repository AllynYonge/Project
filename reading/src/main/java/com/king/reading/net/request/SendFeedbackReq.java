package com.king.reading.net.request;

import com.king.reading.ddb.SendFeedbackRequest;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 31/07/2017.
 */

public class SendFeedbackReq extends BaseRequest<Boolean> {

    public SendFeedbackReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        SendFeedbackRequest request = new SendFeedbackRequest();
        request.setContent((String) params[0]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(Boolean rsp) {
        return rsp;
    }

    @Override
    protected Single<Boolean> getRequestSingle(Api api) {
        return api.sendFeedBack(this);
    }

    @Override
    protected String getRequestName() {
        return SendFeedbackRequest.class.getSimpleName();
    }
}
