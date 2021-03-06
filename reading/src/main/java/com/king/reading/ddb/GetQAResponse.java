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
public class GetQAResponse {

	@TarsStructProperty(order = 0, isRequire = true)
	public java.util.List<QA> lines = null;

	public java.util.List<QA> getLines() {
		return lines;
	}

	public void setLines(java.util.List<QA> lines) {
		this.lines = lines;
	}

	public GetQAResponse() {
	}

	public GetQAResponse(java.util.List<QA> lines) {
		this.lines = lines;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(lines);
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
		if (!(obj instanceof GetQAResponse)) {
			return false;
		}
		GetQAResponse other = (GetQAResponse) obj;
		return (
			TarsUtil.equals(lines, other.lines) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(lines, 0);
	}

	static java.util.List<QA> cache_lines;
	static { 
		cache_lines = new java.util.ArrayList<QA>();
		QA var_29 = new QA();
		cache_lines.add(var_29);
	}

	public void readFrom(TarsInputStream _is) {
		this.lines = (java.util.List<QA>) _is.read(cache_lines, 0, true);
	}

}
