package com.xn.alex.license;

import java.io.Serializable;

public class ValidateDateTime implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7504703925839577086L;
	private String time;

	public ValidateDateTime(String time) {
		super();
		this.time = time;
	}

	public String getTime() {
		return time;
	}

}
