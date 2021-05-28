package com.withTalk.server.repository;

import com.withTalk.server.model.JoinChatRoom;

public interface JoinChatRoomMapper {
	//참여 조회
	public JoinChatRoom select (JoinChatRoom joinChatRoom) throws Exception;
	//참여 삽입
	public void insert (JoinChatRoom joinChatRoom) throws Exception;
	//참여 삭제
	public void delete (JoinChatRoom joinChatRoom) throws Exception;
}
