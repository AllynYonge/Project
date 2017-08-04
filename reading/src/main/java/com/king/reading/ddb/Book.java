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
public class Book {

	@TarsStructProperty(order = 0, isRequire = true)
	public BookBase base = null;
	@TarsStructProperty(order = 1, isRequire = true)
	public java.util.List<Module> modules = null;
	@TarsStructProperty(order = 2, isRequire = true)
	public PageRange pageRange = null;
	@TarsStructProperty(order = 3, isRequire = false)
	public SecKeyPair secKeyPair = null;

	public BookBase getBase() {
		return base;
	}

	public void setBase(BookBase base) {
		this.base = base;
	}

	public java.util.List<Module> getModules() {
		return modules;
	}

	public void setModules(java.util.List<Module> modules) {
		this.modules = modules;
	}

	public PageRange getPageRange() {
		return pageRange;
	}

	public void setPageRange(PageRange pageRange) {
		this.pageRange = pageRange;
	}

	public SecKeyPair getSecKeyPair() {
		return secKeyPair;
	}

	public void setSecKeyPair(SecKeyPair secKeyPair) {
		this.secKeyPair = secKeyPair;
	}

	public Book() {
	}

	public Book(BookBase base, java.util.List<Module> modules, PageRange pageRange, SecKeyPair secKeyPair) {
		this.base = base;
		this.modules = modules;
		this.pageRange = pageRange;
		this.secKeyPair = secKeyPair;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(base);
		result = prime * result + TarsUtil.hashCode(modules);
		result = prime * result + TarsUtil.hashCode(pageRange);
		result = prime * result + TarsUtil.hashCode(secKeyPair);
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
		if (!(obj instanceof Book)) {
			return false;
		}
		Book other = (Book) obj;
		return (
			TarsUtil.equals(base, other.base) &&
			TarsUtil.equals(modules, other.modules) &&
			TarsUtil.equals(pageRange, other.pageRange) &&
			TarsUtil.equals(secKeyPair, other.secKeyPair) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(base, 0);
		_os.write(modules, 1);
		_os.write(pageRange, 2);
		if (null != secKeyPair) {
			_os.write(secKeyPair, 3);
		}
	}

	static BookBase cache_base;
	static { 
		cache_base = new BookBase();
	}
	static java.util.List<Module> cache_modules;
	static { 
		cache_modules = new java.util.ArrayList<Module>();
		Module var_7 = new Module();
		cache_modules.add(var_7);
	}
	static PageRange cache_pageRange;
	static { 
		cache_pageRange = new PageRange();
	}
	static SecKeyPair cache_secKeyPair;
	static { 
		cache_secKeyPair = new SecKeyPair();
	}

	public void readFrom(TarsInputStream _is) {
		this.base = (BookBase) _is.read(cache_base, 0, true);
		this.modules = (java.util.List<Module>) _is.read(cache_modules, 1, true);
		this.pageRange = (PageRange) _is.read(cache_pageRange, 2, true);
		this.secKeyPair = (SecKeyPair) _is.read(cache_secKeyPair, 3, false);
	}

}
