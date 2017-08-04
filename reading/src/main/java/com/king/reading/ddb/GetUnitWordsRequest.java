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
public class GetUnitWordsRequest {

	@TarsStructProperty(order = 0, isRequire = true)
	public int resourceID = 0;
	@TarsStructProperty(order = 1, isRequire = true)
	public long unitID = 0L;

	public int getResourceID() {
		return resourceID;
	}

	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}

	public long getUnitID() {
		return unitID;
	}

	public void setUnitID(long unitID) {
		this.unitID = unitID;
	}

	public GetUnitWordsRequest() {
	}

	public GetUnitWordsRequest(int resourceID, long unitID) {
		this.resourceID = resourceID;
		this.unitID = unitID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(resourceID);
		result = prime * result + TarsUtil.hashCode(unitID);
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
		if (!(obj instanceof GetUnitWordsRequest)) {
			return false;
		}
		GetUnitWordsRequest other = (GetUnitWordsRequest) obj;
		return (
			TarsUtil.equals(resourceID, other.resourceID) &&
			TarsUtil.equals(unitID, other.unitID) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(resourceID, 0);
		_os.write(unitID, 1);
	}


	public void readFrom(TarsInputStream _is) {
		this.resourceID = _is.read(resourceID, 0, true);
		this.unitID = _is.read(unitID, 1, true);
	}

}