package com.withTalk.server.model;

import java.io.Serializable;

public class Member implements Serializable {
	private String id;
	private String name;
	private String password;
	private String phoneNo;
	
	public Member() {
		
	}
	
	public Member(String id, String name, String password, String phoneNo) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.phoneNo = phoneNo;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}
	
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	@Override
	public String toString() {
		return "id : " + id + "name : " + name + "password : " + password + "phoneNo : " + phoneNo;
	}
}
