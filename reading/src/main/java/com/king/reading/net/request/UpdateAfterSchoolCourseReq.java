package com.king.reading.net.request;

import com.king.reading.ddb.UpdateAfterSchoolCourseRequest;
import com.king.reading.net.Api;
import com.king.reading.net.base.BaseRequest;
import com.qq.tars.protocol.tars.TarsOutputStream;

import io.reactivex.Single;

/**
 * Created by AllynYonge on 02/08/2017.
 */

public class UpdateAfterSchoolCourseReq extends BaseRequest<Boolean> {
    public UpdateAfterSchoolCourseReq(Api api, Object... params) {
        super(api, params);
    }

    @Override
    protected byte[] makeReqBinary(TarsOutputStream outputStream, Object[] params) {
        UpdateAfterSchoolCourseRequest request = new UpdateAfterSchoolCourseRequest();
        request.setType((Integer) params[0]);
        request.setCourseID((Long) params[1]);
        request.writeTo(outputStream);
        return outputStream.toByteArray();
    }

    @Override
    protected boolean validateRsp(Boolean rsp) {
        return rsp;
    }

    @Override
    protected Single getRequestSingle(Api api) {
        return api.updateUserSchoolCourse(this);
    }

    @Override
    protected String getRequestName() {
        return UpdateAfterSchoolCourseRequest.class.getSimpleName();
    }
}
