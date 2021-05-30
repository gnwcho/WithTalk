package com.withTalk.server.service;

import java.util.List;

import com.withTalk.server.model.JoinChatRoom;

public interface JoinChatRoomService {
	//참여 조회
	public List<JoinChatRoom> select (JoinChatRoom joinChatRoom) throws Exception;
	//참여 등록
	public String insert (JoinChatRoom joinChatRoom, List<String> receiverId) throws Exception;
	//참여 삭제
	public void delete (JoinChatRoom joinChatRoom) throws Exception;
	
}
