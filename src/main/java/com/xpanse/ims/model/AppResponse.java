package com.xpanse.ims.model;

/** App Response Model class */
public class AppResponse {

	private String message;
	
	public AppResponse(String message){
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
