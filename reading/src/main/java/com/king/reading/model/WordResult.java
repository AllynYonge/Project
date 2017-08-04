package com.king.reading.model;

import java.io.Serializable;

public class WordResult  implements Serializable{
//	private int ID;//自增ID
	private int parentID;//所属模块ID
	private String userName;//所属用户名
	private int Sort;//顺序号
	private String parentText;//所属文本
	private String text;//词的字符串，使用”sil”表示语音中的静音段
	private int type;//类型，共有6种类型，分别是:0 多词，1 漏词，2 正常词， 3错误词，4 静音，5 重复词
	private double begin;//开始时间，单位为秒
	private double end;//结束时间，单位为秒
	private double volume;//音量
	private double score;//分值
	
	public int getParentID() {
		return parentID;
	}
	public void setParentID(int parentID) {
		this.parentID = parentID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getSort() {
		return Sort;
	}
	public void setSort(int sort) {
		Sort = sort;
	}
	public String getParentText() {
		return parentText;
	}
	public void setParentText(String parentText) {
		this.parentText = parentText;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getType() {
		return type;
	}
	public void setType(String type) {
		try{
			this.type=Integer.parseInt(type);
		}catch(Exception e){
			this.type=3;
		}
	}
	public double getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		try{
			this.begin=Double.parseDouble(begin);
		}catch(Exception e){
			this.begin=0;
		}
	}
	public double getEnd() {
		return end;
	}
	public void setEnd(String end) {
		try{
			this.end=Double.parseDouble(end);
		}catch(Exception e){
			this.end=0;
		}
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		try{
			this.volume=Double.parseDouble(volume);
		}catch(Exception e){
			this.volume=0;
		}
	}
	public double getScore() {
		return score;
	}
	public void setScore(String score) {
		try{
			this.score=Double.parseDouble(score);
		}catch(Exception e){
			this.score=0;
		}
	}
	@Override
	public String toString() {
		return "WordResult [text=" + text + ", type=" + type + ", begin="
				+ begin + ", end=" + end + ", volume=" + volume + ", score="
				+ score + "]";
	}
	
	
}
