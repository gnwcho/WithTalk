package com.withTalk.server.repository;

import com.withTalk.server.model.Message;

public interface MessageMapper {
	public int insert(Message message);
	public String selectTime(Message message);
}
