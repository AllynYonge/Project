package com.king.reading.net.request;

import com.king.reading.ddb.GetAchievementRequest;
import com.king.reading.ddb.GetAchievementResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 31/07/2017.
 */

public class GetAchievementReq extends BaseRequest<GetAchievementResponse> {
    public GetAchievementReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetAchievementRequest request = new GetAchievementRequest();
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetAchievementResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetAchievementResponse> getRequestSingle(Api api) {
        return api.getAchievement(this);
    }

    @Override
    protected String getRequestName() {
        return GetAchievementRequest.class.getSimpleName();
    }
}
