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
public class GetSchoolsResponse {

	@TarsStructProperty(order = 0, isRequire = true)
	public java.util.List<School> schools = null;

	public java.util.List<School> getSchools() {
		return schools;
	}

	public void setSchools(java.util.List<School> schools) {
		this.schools = schools;
	}

	public GetSchoolsResponse() {
	}

	public GetSchoolsResponse(java.util.List<School> schools) {
		this.schools = schools;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(schools);
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
		if (!(obj instanceof GetSchoolsResponse)) {
			return false;
		}
		GetSchoolsResponse other = (GetSchoolsResponse) obj;
		return (
			TarsUtil.equals(schools, other.schools) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(schools, 0);
	}

	static java.util.List<School> cache_schools;
	static { 
		cache_schools = new java.util.ArrayList<School>();
		School var_32 = new School();
		cache_schools.add(var_32);
	}

	public void readFrom(TarsInputStream _is) {
		this.schools = (java.util.List<School>) _is.read(cache_schools, 0, true);
	}

}