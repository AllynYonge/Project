// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.king.reading.mod;

import com.qq.tars.protocol.util.*;
import com.qq.tars.protocol.annotation.*;
import com.qq.tars.protocol.tars.*;
import com.qq.tars.protocol.tars.annotation.*;

@TarsStruct
public class Device {

	@TarsStructProperty(order = 1, isRequire = false)
	public String sdkName = "";
	@TarsStructProperty(order = 2, isRequire = false)
	public int sdkVersion = 0;
	@TarsStructProperty(order = 3, isRequire = false)
	public String brand = "";
	@TarsStructProperty(order = 4, isRequire = false)
	public String model = "";
	@TarsStructProperty(order = 5, isRequire = false)
	public String versionName = "";
	@TarsStructProperty(order = 6, isRequire = false)
	public int versionCode = 0;
	@TarsStructProperty(order = 7, isRequire = false)
	public int buildNum = 0;
	@TarsStructProperty(order = 8, isRequire = false)
	public String channel = "";

	public String getSdkName() {
		return sdkName;
	}

	public void setSdkName(String sdkName) {
		this.sdkName = sdkName;
	}

	public int getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(int sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getBuildNum() {
		return buildNum;
	}

	public void setBuildNum(int buildNum) {
		this.buildNum = buildNum;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Device() {
	}

	public Device(String sdkName, int sdkVersion, String brand, String model, String versionName, int versionCode, int buildNum, String channel) {
		this.sdkName = sdkName;
		this.sdkVersion = sdkVersion;
		this.brand = brand;
		this.model = model;
		this.versionName = versionName;
		this.versionCode = versionCode;
		this.buildNum = buildNum;
		this.channel = channel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(sdkName);
		result = prime * result + TarsUtil.hashCode(sdkVersion);
		result = prime * result + TarsUtil.hashCode(brand);
		result = prime * result + TarsUtil.hashCode(model);
		result = prime * result + TarsUtil.hashCode(versionName);
		result = prime * result + TarsUtil.hashCode(versionCode);
		result = prime * result + TarsUtil.hashCode(buildNum);
		result = prime * result + TarsUtil.hashCode(channel);
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
		if (!(obj instanceof Device)) {
			return false;
		}
		Device other = (Device) obj;
		return (
			TarsUtil.equals(sdkName, other.sdkName) &&
			TarsUtil.equals(sdkVersion, other.sdkVersion) &&
			TarsUtil.equals(brand, other.brand) &&
			TarsUtil.equals(model, other.model) &&
			TarsUtil.equals(versionName, other.versionName) &&
			TarsUtil.equals(versionCode, other.versionCode) &&
			TarsUtil.equals(buildNum, other.buildNum) &&
			TarsUtil.equals(channel, other.channel) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		if (null != sdkName) {
			_os.write(sdkName, 1);
		}
		_os.write(sdkVersion, 2);
		if (null != brand) {
			_os.write(brand, 3);
		}
		if (null != model) {
			_os.write(model, 4);
		}
		if (null != versionName) {
			_os.write(versionName, 5);
		}
		_os.write(versionCode, 6);
		_os.write(buildNum, 7);
		if (null != channel) {
			_os.write(channel, 8);
		}
	}


	public void readFrom(TarsInputStream _is) {
		this.sdkName = _is.readString(1, false);
		this.sdkVersion = _is.read(sdkVersion, 2, false);
		this.brand = _is.readString(3, false);
		this.model = _is.readString(4, false);
		this.versionName = _is.readString(5, false);
		this.versionCode = _is.read(versionCode, 6, false);
		this.buildNum = _is.read(buildNum, 7, false);
		this.channel = _is.readString(8, false);
	}

}
