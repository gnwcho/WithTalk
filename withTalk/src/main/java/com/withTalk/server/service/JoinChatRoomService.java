package com.withTalk.server.service;

import java.util.List;

import com.withTalk.server.model.JoinChatRoom;

public interface JoinChatRoomService {
	//참여 조회
	public List<JoinChatRoom> select (JoinChatRoom joinChatRoom) throws Exception;
	//모든 참여 대화방 조회
	public List<JoinChatRoom> selectAllChatRoom (List<JoinChatRoom> joinChatRoomList) throws Exception;
	//단체방 참여 등록
	public String insert (JoinChatRoom joinChatRoom, List<String> receiverId) throws Exception;
	//개인방 참여 등록
	public String insert (JoinChatRoom joinChatRooms, List<String> receiverId, String senderId) throws Exception;
	//참여 대화방 이름 변경
	public int update (JoinChatRoom joinChatRoom) throws Exception;
	//참여 삭제
	public String delete (JoinChatRoom joinChatRoom) throws Exception;
	//참여 중복 제거 조회
	public List selectDistinctNo (JoinChatRoom joinChatRoom) throws Exception;
}
