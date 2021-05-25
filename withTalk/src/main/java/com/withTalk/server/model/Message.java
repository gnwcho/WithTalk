package com.withTalk.server.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Message implements Serializable {
	private int sequenceNo;
	private LocalDate sendTime;
	private String contents;
	private int chatRoomNo;
	private String senderId;
	
	public Message() {
		
	}
	
	public Message(int sequenceNo, LocalDate sendTime, String contents, int chatRoomNo, String senderId) {
		this.sequenceNo = sequenceNo;
		this.sendTime = sendTime;
		this.contents = contents;
		this.chatRoomNo = chatRoomNo;
		this.senderId = senderId;
	}
	
	public int getSequenceNo() {
		return sequenceNo;
	}
	
	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	public LocalDate getSendTime() {
		return sendTime;
	}
	
	public void setSendTime(LocalDate sendTime) {
		this.sendTime = sendTime;
	}
	
	public String getContents() {
		return contents;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public int getChatRoomNo() {
		return chatRoomNo;
	}
	
	public void setChatRoomNo(int chatRoomNo) {
		this.chatRoomNo = chatRoomNo;
	}
	
	public String getSenderId() {
		return senderId;
	}
	
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
}
