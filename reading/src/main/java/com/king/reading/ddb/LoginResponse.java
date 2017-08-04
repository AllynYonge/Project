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
public class LoginResponse {

	@TarsStructProperty(order = 0, isRequire = true)
	public User userInfo = null;
	@TarsStructProperty(order = 1, isRequire = false)
	public int isFirstLogin = 0;

	public User getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}

	public int getIsFirstLogin() {
		return isFirstLogin;
	}

	public void setIsFirstLogin(int isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public LoginResponse() {
	}

	public LoginResponse(User userInfo, int isFirstLogin) {
		this.userInfo = userInfo;
		this.isFirstLogin = isFirstLogin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(userInfo);
		result = prime * result + TarsUtil.hashCode(isFirstLogin);
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
		if (!(obj instanceof LoginResponse)) {
			return false;
		}
		LoginResponse other = (LoginResponse) obj;
		return (
			TarsUtil.equals(userInfo, other.userInfo) &&
			TarsUtil.equals(isFirstLogin, other.isFirstLogin) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(userInfo, 0);
		_os.write(isFirstLogin, 1);
	}

	static User cache_userInfo;
	static { 
		cache_userInfo = new User();
	}

	public void readFrom(TarsInputStream _is) {
		this.userInfo = (User) _is.read(cache_userInfo, 0, true);
		this.isFirstLogin = _is.read(isFirstLogin, 1, false);
	}

}
