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
public class School {

	@TarsStructProperty(order = 0, isRequire = true)
	public long schoolId = 0L;
	@TarsStructProperty(order = 1, isRequire = true)
	public String name = "";

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public School() {
	}

	public School(long schoolId, String name) {
		this.schoolId = schoolId;
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(schoolId);
		result = prime * result + TarsUtil.hashCode(name);
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
		if (!(obj instanceof School)) {
			return false;
		}
		School other = (School) obj;
		return (
			TarsUtil.equals(schoolId, other.schoolId) &&
			TarsUtil.equals(name, other.name) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(schoolId, 0);
		_os.write(name, 1);
	}


	public void readFrom(TarsInputStream _is) {
		this.schoolId = _is.read(schoolId, 0, true);
		this.name = _is.readString(1, true);
	}

}
