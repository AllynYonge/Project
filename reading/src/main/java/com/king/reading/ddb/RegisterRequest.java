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
public class RegisterRequest {

	@TarsStructProperty(order = 0, isRequire = true)
	public String mobilePhone = "";
	@TarsStructProperty(order = 1, isRequire = true)
	public String verifyCode = "";
	@TarsStructProperty(order = 2, isRequire = true)
	public String password = "";

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public RegisterRequest() {
	}

	public RegisterRequest(String mobilePhone, String verifyCode, String password) {
		this.mobilePhone = mobilePhone;
		this.verifyCode = verifyCode;
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(mobilePhone);
		result = prime * result + TarsUtil.hashCode(verifyCode);
		result = prime * result + TarsUtil.hashCode(password);
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
		if (!(obj instanceof RegisterRequest)) {
			return false;
		}
		RegisterRequest other = (RegisterRequest) obj;
		return (
			TarsUtil.equals(mobilePhone, other.mobilePhone) &&
			TarsUtil.equals(verifyCode, other.verifyCode) &&
			TarsUtil.equals(password, other.password) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(mobilePhone, 0);
		_os.write(verifyCode, 1);
		_os.write(password, 2);
	}


	public void readFrom(TarsInputStream _is) {
		this.mobilePhone = _is.readString(0, true);
		this.verifyCode = _is.readString(1, true);
		this.password = _is.readString(2, true);
	}

}
