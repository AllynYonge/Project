// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.king.reading.ddb;

import com.qq.tars.protocol.util.*;
import com.qq.tars.protocol.annotation.*;
import com.qq.tars.protocol.tars.*;
import com.qq.tars.protocol.tars.annotation.*;

@TarsStruct
public class UpdateAfterSchoolCourseRequest {

	@TarsStructProperty(order = 0, isRequire = true)
	public int type = 0;
	@TarsStructProperty(order = 1, isRequire = true)
	public long courseID = 0L;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getCourseID() {
		return courseID;
	}

	public void setCourseID(long courseID) {
		this.courseID = courseID;
	}

	public UpdateAfterSchoolCourseRequest() {
	}

	public UpdateAfterSchoolCourseRequest(int type, long courseID) {
		this.type = type;
		this.courseID = courseID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(type);
		result = prime * result + TarsUtil.hashCode(courseID);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof UpdateAfterSchoolCourseRequest)) {
			return false;
		}
		UpdateAfterSchoolCourseRequest other = (UpdateAfterSchoolCourseRequest) obj;
		return (
			TarsUtil.equals(type, other.type) &&
			TarsUtil.equals(courseID, other.courseID) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(type, 0);
		_os.write(courseID, 1);
	}


	public void readFrom(TarsInputStream _is) {
		this.type = _is.read(type, 0, true);
		this.courseID = _is.read(courseID, 1, true);
	}

}
