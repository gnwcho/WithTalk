package com.withTalk.server.model;

import java.io.Serializable;
import java.time.LocalDate;

public class JoinChatRoom implements Serializable {
	private int sequenceNo;
	private String chatRoomName;
	private LocalDate joinTime;
	private int chatRoomNo;
	private String memberId;
	
	public JoinChatRoom() {

	}

	public JoinChatRoom(int sequenceNo, String chatRoomName, LocalDate joinTime, int chatRoomNo, String memberId) {
		this.sequenceNo = sequenceNo;
		this.chatRoomName = chatRoomName;
		this.joinTime = joinTime;
		this.chatRoomNo = chatRoomNo;
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

	public int getChatRoomNo() {
		return chatRoomNo;
	}

	public void setChatRoomNo(int chatRoomNo) {
		this.chatRoomNo = chatRoomNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
}
