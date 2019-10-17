package com.softplayer.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReturnObject {
	
	private int status;
	private String message;
	private Object result;
	
	public ReturnObject setReturnObjectOk(Object result) {
		this.status = 200;
		this.message = null;
		this.result = result;
		return this;
	}
	
	public ReturnObject setReturnObjectError(int status, String message) {
		this.status = status;
		this.message = message;
		this.result = null;
		return this;
	}
}
