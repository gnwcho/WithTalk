package com.withTalk.server.service;

import java.util.List;

import com.withTalk.server.model.JoinChatRoom;

public interface JoinChatRoomService {
	//참여 조회
	public List<JoinChatRoom> select (JoinChatRoom joinChatRoom) throws Exception;
	//단체방 참여 등록
	public String insert (JoinChatRoom joinChatRoom, List<String> receiverId) throws Exception;
	//개인방 참여 등록
	public String insert (JoinChatRoom joinChatRooms, List<String> receiverId, String senderId) throws Exception;
	//참여 삭제
	public String delete (JoinChatRoom joinChatRoom) throws Exception;
	
}
