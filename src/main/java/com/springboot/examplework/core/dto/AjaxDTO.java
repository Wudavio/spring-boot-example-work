package com.springboot.examplework.core.dto;

public class AjaxDTO {
	public static final String ACTION_LOGOUT = "logout";
	
	private static final int SUCCESS = 1;
	private static final int FAIL = 0;
	
	private int status = FAIL;

	private String message = "";

	private Object data;

	public int getStatus() {
		return status;
	}

	public void setStatusOK() {
		this.status = SUCCESS;
	}

	public void setStatusFail() {
		this.status = FAIL;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
