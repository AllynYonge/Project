package com.king.reading.net.request;

import com.king.reading.ddb.PostReadAfterMeScoreRequest;
import com.king.reading.ddb.Score;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 31/07/2017.
 */

public class PostReadAfterMeScoreReq extends BaseRequest<Boolean> {
    public PostReadAfterMeScoreReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        PostReadAfterMeScoreRequest request = new PostReadAfterMeScoreRequest();
        request.setBookID((Long) params[0]);
        request.setResult((Score) params[1]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(Boolean rsp) {
        return rsp;
    }

    @Override
    protected Single<Boolean> getRequestSingle(Api api) {
        return api.postReadAfterMeScore(this);
    }

    @Override
    protected String getRequestName() {
        return PostReadAfterMeScoreRequest.class.getSimpleName();
    }
}
