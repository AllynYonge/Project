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
public class Word {

	@TarsStructProperty(order = 0, isRequire = true)
	public String word = "";
	@TarsStructProperty(order = 1, isRequire = true)
	public String mean = "";
	@TarsStructProperty(order = 2, isRequire = true)
	public String encryptSoundURL = "";

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getMean() {
		return mean;
	}

	public void setMean(String mean) {
		this.mean = mean;
	}

	public String getEncryptSoundURL() {
		return encryptSoundURL;
	}

	public void setEncryptSoundURL(String encryptSoundURL) {
		this.encryptSoundURL = encryptSoundURL;
	}

	public Word() {
	}

	public Word(String word, String mean, String encryptSoundURL) {
		this.word = word;
		this.mean = mean;
		this.encryptSoundURL = encryptSoundURL;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(word);
		result = prime * result + TarsUtil.hashCode(mean);
		result = prime * result + TarsUtil.hashCode(encryptSoundURL);
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
		if (!(obj instanceof Word)) {
			return false;
		}
		Word other = (Word) obj;
		return (
			TarsUtil.equals(word, other.word) &&
			TarsUtil.equals(mean, other.mean) &&
			TarsUtil.equals(encryptSoundURL, other.encryptSoundURL) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(word, 0);
		_os.write(mean, 1);
		_os.write(encryptSoundURL, 2);
	}


	public void readFrom(TarsInputStream _is) {
		this.word = _is.readString(0, true);
		this.mean = _is.readString(1, true);
		this.encryptSoundURL = _is.readString(2, true);
	}

}
