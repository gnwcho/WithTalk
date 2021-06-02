package com.withTalk.server.service;

import com.withTalk.server.model.Message;

public interface ChatService {
	public Message sendMessage(Message message) throws Exception;
	public String messageTime(Message message) throws Exception;
}
