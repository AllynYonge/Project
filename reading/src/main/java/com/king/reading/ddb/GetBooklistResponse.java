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
public class GetBooklistResponse {

	@TarsStructProperty(order = 0, isRequire = true)
	public java.util.List<BookVersion> areas = null;

	public java.util.List<BookVersion> getAreas() {
		return areas;
	}

	public void setAreas(java.util.List<BookVersion> areas) {
		this.areas = areas;
	}

	public GetBooklistResponse() {
	}

	public GetBooklistResponse(java.util.List<BookVersion> areas) {
		this.areas = areas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(areas);
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
		if (!(obj instanceof GetBooklistResponse)) {
			return false;
		}
		GetBooklistResponse other = (GetBooklistResponse) obj;
		return (
			TarsUtil.equals(areas, other.areas) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(areas, 0);
	}

	static java.util.List<BookVersion> cache_areas;
	static { 
		cache_areas = new java.util.ArrayList<BookVersion>();
		BookVersion var_18 = new BookVersion();
		cache_areas.add(var_18);
	}

	public void readFrom(TarsInputStream _is) {
		this.areas = (java.util.List<BookVersion>) _is.read(cache_areas, 0, true);
	}

}