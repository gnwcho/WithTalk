package com.withTalk.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.withTalk.server.model.Message;
import com.withTalk.server.repository.MessageMapper;

@Service
public class ChatServiceImpl implements ChatService{
	@Autowired
	private MessageMapper messageMapper;
	
	@Override
	public String sendMessage(Message message) throws Exception {
		String result = null;
		
		if (messageMapper.insert(message) == 1) {
			result = "r200";
		} else {
			result = "r400";
		}
		
		return result;
	}
}
