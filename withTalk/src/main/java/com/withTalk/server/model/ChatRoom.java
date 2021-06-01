package com.withTalk.server.model;

import java.io.Serializable;

public class ChatRoom implements Serializable {
	private int sequenceNo;
	private int userCount;
	private String type;
	
	public ChatRoom() {

	}
	
	public ChatRoom(int sequenceNo, int userCount, String type) {
		this.sequenceNo = sequenceNo;
		this.userCount = userCount;
		this.type = type;

	}
	public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	public int getUserCount() {
		return userCount;
	}
	
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
