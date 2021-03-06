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
public class City {

	@TarsStructProperty(order = 0, isRequire = true)
	public String name = "";
	@TarsStructProperty(order = 1, isRequire = true)
	public java.util.List<District> districts = null;
	@TarsStructProperty(order = 2, isRequire = false)
	public int areaCode = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public java.util.List<District> getDistricts() {
		return districts;
	}

	public void setDistricts(java.util.List<District> districts) {
		this.districts = districts;
	}

	public int getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(int areaCode) {
		this.areaCode = areaCode;
	}

	public City() {
	}

	public City(String name, java.util.List<District> districts, int areaCode) {
		this.name = name;
		this.districts = districts;
		this.areaCode = areaCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(name);
		result = prime * result + TarsUtil.hashCode(districts);
		result = prime * result + TarsUtil.hashCode(areaCode);
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
		if (!(obj instanceof City)) {
			return false;
		}
		City other = (City) obj;
		return (
			TarsUtil.equals(name, other.name) &&
			TarsUtil.equals(districts, other.districts) &&
			TarsUtil.equals(areaCode, other.areaCode) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(name, 0);
		_os.write(districts, 1);
		_os.write(areaCode, 2);
	}

	static java.util.List<District> cache_districts;
	static { 
		cache_districts = new java.util.ArrayList<District>();
		District var_16 = new District();
		cache_districts.add(var_16);
	}

	public void readFrom(TarsInputStream _is) {
		this.name = _is.readString(0, true);
		this.districts = (java.util.List<District>) _is.read(cache_districts, 1, true);
		this.areaCode = _is.read(areaCode, 2, false);
	}

}
