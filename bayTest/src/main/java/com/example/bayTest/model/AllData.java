package com.example.bayTest.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AllData {
	@Id
	private long id;
	private String uri;
	private String reqObj;
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
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getReqObj() {
		return reqObj;
	}
	public void setReqObj(String reqObj) {
		this.reqObj = reqObj;
	}
	
	
}
