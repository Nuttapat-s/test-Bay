package com.example.bayTest.model;

import java.util.Date;

public class AllDataObj {
	private long id;
	private String uri;
	private ReqObj reqObj;
	private String dateTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public ReqObj getReqObj() {
		return reqObj;
	}
	public void setReqObj(ReqObj reqObj) {
		this.reqObj = reqObj;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	
}
