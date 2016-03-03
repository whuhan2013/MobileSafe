package com.zj.mobilesafe.domain;

public class BlackNumberInfo {
	
	private String number;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	private String mode;
	@Override
	public String toString() {
		return "BlackNumberInfo [number=" + number + ", mode=" + mode + "]";
	}
	
	

}
