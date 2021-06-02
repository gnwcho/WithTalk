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
	public Message sendMessage(Message message) throws Exception {
		String result = null;
		if (messageMapper.insert(message) == 1) {
			message.setSendTime(this.messageTime(message));
		} 
		
		return message;
	}

	@Override
	public String messageTime(Message message) throws Exception {
		return messageMapper.selectTime(message);
	}
}
