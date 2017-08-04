package com.king.reading.net.request;

import com.king.reading.ddb.GetReadAfterMeGameBoardRequest;
import com.king.reading.ddb.GetReadAfterMeGameBoardResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 31/07/2017.
 */

public class GetReadAfterMeGameBoardReq extends BaseRequest<GetReadAfterMeGameBoardResponse> {
    public GetReadAfterMeGameBoardReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetReadAfterMeGameBoardRequest request = new GetReadAfterMeGameBoardRequest();
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetReadAfterMeGameBoardResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetReadAfterMeGameBoardResponse> getRequestSingle(Api api) {
        return api.getReadAfterMeGameBoard(this);
    }

    @Override
    protected String getRequestName() {
        return GetReadAfterMeGameBoardRequest.class.getSimpleName();
    }
}
