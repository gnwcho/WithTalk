package com.withTalk.server.model;

import java.io.Serializable;
import java.time.LocalDate;

public class JoinChatRoom implements Serializable {
	private int sequenceNo;
	private String chatRoomName;
	private LocalDate joinTime;
	private String memberId;
	
	public JoinChatRoom() {

	}
	
	public JoinChatRoom(int sequenceNo, String chatRoomName, LocalDate joinTime, String memberId) {
		this.sequenceNo = sequenceNo;
		this.chatRoomName = chatRoomName;
		this.joinTime = joinTime;
		this.memberId = memberId;
	}

	public int getSequenceNo() {
		return sequenceNo;
	}
	
	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	public String getChatRoomName() {
		return chatRoomName;
	}
	
	public void setChatRoomName(String chatRoomName) {
		this.chatRoomName = chatRoomName;
	}
	
	public LocalDate getJoinTime() {
		return joinTime;
	}
	
	public void setJoinTime(LocalDate joinTime) {
		this.joinTime = joinTime;
	}
	
	public String getMemberId() {
		return memberId;
	}
	
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
}
