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
public class Header {

	@TarsStructProperty(order = 1, isRequire = true)
	public int requestId = 0;
	@TarsStructProperty(order = 2, isRequire = true)
	public String requestName = "";
	@TarsStructProperty(order = 3, isRequire = true)
	public Device device = null;
	@TarsStructProperty(order = 4, isRequire = false)
	public long svrTimestamp = 0L;
	@TarsStructProperty(order = 5, isRequire = false)
	public String token = "";
	@TarsStructProperty(order = 6, isRequire = false)
	public String refreshToken = "";
	@TarsStructProperty(order = 7, isRequire = false)
	public String account = "";
	@TarsStructProperty(order = 8, isRequire = false)
	public long userId = 0L;
	@TarsStructProperty(order = 9, isRequire = true)
	public String appId = "";
	@TarsStructProperty(order = 10, isRequire = true)
	public int platform = 0;
	@TarsStructProperty(order = 11, isRequire = false)
	public java.util.Map<String, String> ext = null;

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public long getSvrTimestamp() {
		return svrTimestamp;
	}

	public void setSvrTimestamp(long svrTimestamp) {
		this.svrTimestamp = svrTimestamp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public java.util.Map<String, String> getExt() {
		return ext;
	}

	public void setExt(java.util.Map<String, String> ext) {
		this.ext = ext;
	}

	public Header() {
	}

	public Header(int requestId, String requestName, Device device, long svrTimestamp, String token, String refreshToken, String account, long userId, String appId, int platform, java.util.Map<String, String> ext) {
		this.requestId = requestId;
		this.requestName = requestName;
		this.device = device;
		this.svrTimestamp = svrTimestamp;
		this.token = token;
		this.refreshToken = refreshToken;
		this.account = account;
		this.userId = userId;
		this.appId = appId;
		this.platform = platform;
		this.ext = ext;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(requestId);
		result = prime * result + TarsUtil.hashCode(requestName);
		result = prime * result + TarsUtil.hashCode(device);
		result = prime * result + TarsUtil.hashCode(svrTimestamp);
		result = prime * result + TarsUtil.hashCode(token);
		result = prime * result + TarsUtil.hashCode(refreshToken);
		result = prime * result + TarsUtil.hashCode(account);
		result = prime * result + TarsUtil.hashCode(userId);
		result = prime * result + TarsUtil.hashCode(appId);
		result = prime * result + TarsUtil.hashCode(platform);
		result = prime * result + TarsUtil.hashCode(ext);
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
		if (!(obj instanceof Header)) {
			return false;
		}
		Header other = (Header) obj;
		return (
			TarsUtil.equals(requestId, other.requestId) &&
			TarsUtil.equals(requestName, other.requestName) &&
			TarsUtil.equals(device, other.device) &&
			TarsUtil.equals(svrTimestamp, other.svrTimestamp) &&
			TarsUtil.equals(token, other.token) &&
			TarsUtil.equals(refreshToken, other.refreshToken) &&
			TarsUtil.equals(account, other.account) &&
			TarsUtil.equals(userId, other.userId) &&
			TarsUtil.equals(appId, other.appId) &&
			TarsUtil.equals(platform, other.platform) &&
			TarsUtil.equals(ext, other.ext) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(requestId, 1);
		_os.write(requestName, 2);
		_os.write(device, 3);
		_os.write(svrTimestamp, 4);
		if (null != token) {
			_os.write(token, 5);
		}
		if (null != refreshToken) {
			_os.write(refreshToken, 6);
		}
		if (null != account) {
			_os.write(account, 7);
		}
		_os.write(userId, 8);
		_os.write(appId, 9);
		_os.write(platform, 10);
		if (null != ext) {
			_os.write(ext, 11);
		}
	}

	static Device cache_device;
	static { 
		cache_device = new Device();
	}
	static java.util.Map<String, String> cache_ext;
	static { 
		cache_ext = new java.util.HashMap<String, String>();
		String var_1 = "";
		String var_2 = "";
		cache_ext.put(var_1 ,var_2);
	}

	public void readFrom(TarsInputStream _is) {
		this.requestId = _is.read(requestId, 1, true);
		this.requestName = _is.readString(2, true);
		this.device = (Device) _is.read(cache_device, 3, true);
		this.svrTimestamp = _is.read(svrTimestamp, 4, false);
		this.token = _is.readString(5, false);
		this.refreshToken = _is.readString(6, false);
		this.account = _is.readString(7, false);
		this.userId = _is.read(userId, 8, false);
		this.appId = _is.readString(9, true);
		this.platform = _is.read(platform, 10, true);
		this.ext = (java.util.Map<String, String>) _is.read(cache_ext, 11, false);
	}

}