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
public class GetAllAfterSchoolCourseResponse {

	@TarsStructProperty(order = 0, isRequire = true)
	public java.util.List<Course> courses = null;

	public java.util.List<Course> getCourses() {
		return courses;
	}

	public void setCourses(java.util.List<Course> courses) {
		this.courses = courses;
	}

	public GetAllAfterSchoolCourseResponse() {
	}

	public GetAllAfterSchoolCourseResponse(java.util.List<Course> courses) {
		this.courses = courses;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(courses);
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
		if (!(obj instanceof GetAllAfterSchoolCourseResponse)) {
			return false;
		}
		GetAllAfterSchoolCourseResponse other = (GetAllAfterSchoolCourseResponse) obj;
		return (
			TarsUtil.equals(courses, other.courses) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(courses, 0);
	}

	static java.util.List<Course> cache_courses;
	static { 
		cache_courses = new java.util.ArrayList<Course>();
		Course var_25 = new Course();
		cache_courses.add(var_25);
	}

	public void readFrom(TarsInputStream _is) {
		this.courses = (java.util.List<Course>) _is.read(cache_courses, 0, true);
	}

}
