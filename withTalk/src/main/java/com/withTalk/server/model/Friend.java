package com.withTalk.server.model;

import java.io.Serializable;

public class Friend implements Serializable{
	private int sequenceNo;
	private String memberId;
	private String friendId;
	
	public Friend() {

	}

	public Friend(int sequenceNo, String memberId, String friendId) {
		this.sequenceNo = sequenceNo;
		this.memberId = memberId;
		this.friendId = friendId;
	}

	public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	
	@Override
	public String toString() {
		return "memberId : " + this.getMemberId() + " || friendId : " + this.getFriendId();
	}
}
