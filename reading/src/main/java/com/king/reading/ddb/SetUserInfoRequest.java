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
public class SetUserInfoRequest {

	@TarsStructProperty(order = 0, isRequire = true)
	public int type = 0;
	@TarsStructProperty(order = 1, isRequire = true)
	public String value = "";

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SetUserInfoRequest() {
	}

	public SetUserInfoRequest(int type, String value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(type);
		result = prime * result + TarsUtil.hashCode(value);
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
		if (!(obj instanceof SetUserInfoRequest)) {
			return false;
		}
		SetUserInfoRequest other = (SetUserInfoRequest) obj;
		return (
			TarsUtil.equals(type, other.type) &&
			TarsUtil.equals(value, other.value) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(type, 0);
		_os.write(value, 1);
	}


	public void readFrom(TarsInputStream _is) {
		this.type = _is.read(type, 0, true);
		this.value = _is.readString(1, true);
	}

}