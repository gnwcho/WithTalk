package com.withTalk.server.repository;

import java.util.List;

import com.withTalk.server.model.ChatRoom;
import com.withTalk.server.model.JoinChatRoom;

public interface JoinChatRoomMapper {
	//참여 조회
	public JoinChatRoom select (JoinChatRoom joinChatRoom) throws Exception;
	//참여 삽입
	public void insert (JoinChatRoom joinChatRoom) throws Exception;
	//참여 삭제
	public void delete (JoinChatRoom joinChatRoom) throws Exception;
	//방 id에 속한 회원 찾기
	public List<String> selectMemberByRoomId(ChatRoom chatRoom) throws Exception;
}
