package com.king.reading.net.request;

import com.king.reading.ddb.GetAfterSchoolCourseRequest;
import com.king.reading.ddb.GetAfterSchoolCourseResponse;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 31/07/2017.
 */

public class GetAfterSchoolCourseReq extends BaseRequest<GetAfterSchoolCourseResponse> {
    public GetAfterSchoolCourseReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        GetAfterSchoolCourseRequest request = new GetAfterSchoolCourseRequest();
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(GetAfterSchoolCourseResponse rsp) {
        return true;
    }

    @Override
    protected Single<GetAfterSchoolCourseResponse> getRequestSingle(Api api) {
        return api.getUserExtensionCourse(this);
    }

    @Override
    protected String getRequestName() {
        return GetAfterSchoolCourseRequest.class.getSimpleName();
    }
}
