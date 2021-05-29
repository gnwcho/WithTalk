package com.withTalk.server.service;

import com.withTalk.server.model.Message;

public interface ChatService {
	public String sendMessage(Message message) throws Exception;
}
