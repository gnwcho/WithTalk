package com.withTalk.server.model;

import java.io.Serializable;

public class ChatRoom implements Serializable {
	private int sequenceNo;
	private int userCount;
	
	public ChatRoom() {

	}
	
	public ChatRoom(int sequenceNo, int userCount) {
		this.sequenceNo = sequenceNo;
		this.userCount = userCount;
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
}
