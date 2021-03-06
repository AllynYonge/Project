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
public class GetBannersRequest {

	@TarsStructProperty(order = 0, isRequire = false)
	public int type = 0;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public GetBannersRequest() {
	}

	public GetBannersRequest(int type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(type);
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
		if (!(obj instanceof GetBannersRequest)) {
			return false;
		}
		GetBannersRequest other = (GetBannersRequest) obj;
		return (
			TarsUtil.equals(type, other.type) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(type, 0);
	}


	public void readFrom(TarsInputStream _is) {
		this.type = _is.read(type, 0, false);
	}

}
