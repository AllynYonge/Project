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
public class GetReadAfterMeInfoRequest {

	@TarsStructProperty(order = 0, isRequire = true)
	public long bookID = 0L;

	public long getBookID() {
		return bookID;
	}

	public void setBookID(long bookID) {
		this.bookID = bookID;
	}

	public GetReadAfterMeInfoRequest() {
	}

	public GetReadAfterMeInfoRequest(long bookID) {
		this.bookID = bookID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(bookID);
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
		if (!(obj instanceof GetReadAfterMeInfoRequest)) {
			return false;
		}
		GetReadAfterMeInfoRequest other = (GetReadAfterMeInfoRequest) obj;
		return (
			TarsUtil.equals(bookID, other.bookID) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(bookID, 0);
	}


	public void readFrom(TarsInputStream _is) {
		this.bookID = _is.read(bookID, 0, true);
	}

}
