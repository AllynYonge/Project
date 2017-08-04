package com.king.reading.net.request;

import com.king.reading.ddb.GetAfterSchoolCourseRequest;
import com.king.reading.ddb.GetAllAfterSchoolCourseResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 31/07/2017.
 */

public class GetAllAfterSchoolCourseReq extends BaseRequest<GetAllAfterSchoolCourseResponse> {
    public GetAllAfterSchoolCourseReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetAfterSchoolCourseRequest request = new GetAfterSchoolCourseRequest();
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetAllAfterSchoolCourseResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetAllAfterSchoolCourseResponse> getRequestSingle(Api api) {
        return api.getAllExtensionCourse(this);
    }

    @Override
    protected String getRequestName() {
        return GetAfterSchoolCourseRequest.class.getSimpleName();
    }
}
