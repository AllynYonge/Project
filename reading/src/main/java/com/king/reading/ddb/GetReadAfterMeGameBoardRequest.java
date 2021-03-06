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
public class GetReadAfterMeGameBoardRequest {

	@TarsStructProperty(order = 0, isRequire = false)
	public String reserved = "";

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public GetReadAfterMeGameBoardRequest() {
	}

	public GetReadAfterMeGameBoardRequest(String reserved) {
		this.reserved = reserved;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(reserved);
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
		if (!(obj instanceof GetReadAfterMeGameBoardRequest)) {
			return false;
		}
		GetReadAfterMeGameBoardRequest other = (GetReadAfterMeGameBoardRequest) obj;
		return (
			TarsUtil.equals(reserved, other.reserved) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		if (null != reserved) {
			_os.write(reserved, 0);
		}
	}


	public void readFrom(TarsInputStream _is) {
		this.reserved = _is.readString(0, false);
	}

}
