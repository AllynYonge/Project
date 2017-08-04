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
public class SecKeyPair {

	@TarsStructProperty(order = 0, isRequire = true)
	public int resourceID = 0;
	@TarsStructProperty(order = 1, isRequire = true)
	public String secKey = "";

	public int getResourceID() {
		return resourceID;
	}

	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}

	public String getSecKey() {
		return secKey;
	}

	public void setSecKey(String secKey) {
		this.secKey = secKey;
	}

	public SecKeyPair() {
	}

	public SecKeyPair(int resourceID, String secKey) {
		this.resourceID = resourceID;
		this.secKey = secKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(resourceID);
		result = prime * result + TarsUtil.hashCode(secKey);
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
		if (!(obj instanceof SecKeyPair)) {
			return false;
		}
		SecKeyPair other = (SecKeyPair) obj;
		return (
			TarsUtil.equals(resourceID, other.resourceID) &&
			TarsUtil.equals(secKey, other.secKey) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(resourceID, 0);
		_os.write(secKey, 1);
	}


	public void readFrom(TarsInputStream _is) {
		this.resourceID = _is.read(resourceID, 0, true);
		this.secKey = _is.readString(1, true);
	}

}