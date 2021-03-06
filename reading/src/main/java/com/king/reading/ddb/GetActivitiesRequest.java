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
public class GetActivitiesRequest {

	@TarsStructProperty(order = 0, isRequire = false)
	public PageContext pageContext = null;

	public PageContext getPageContext() {
		return pageContext;
	}

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	public GetActivitiesRequest() {
	}

	public GetActivitiesRequest(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(pageContext);
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
		if (!(obj instanceof GetActivitiesRequest)) {
			return false;
		}
		GetActivitiesRequest other = (GetActivitiesRequest) obj;
		return (
			TarsUtil.equals(pageContext, other.pageContext) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		if (null != pageContext) {
			_os.write(pageContext, 0);
		}
	}

	static PageContext cache_pageContext;
	static { 
		cache_pageContext = new PageContext();
	}

	public void readFrom(TarsInputStream _is) {
		this.pageContext = (PageContext) _is.read(cache_pageContext, 0, false);
	}

}
