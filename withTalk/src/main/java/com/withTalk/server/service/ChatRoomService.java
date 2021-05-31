package com.withTalk.server.service;

import java.util.List;

import com.withTalk.server.model.ChatRoom;

public interface ChatRoomService {
		//대화방 생성
		public String insert(ChatRoom chatRoom, List<String> receiverId) throws Exception;
		//대화방 매핑
		public void registerMap(int roomId, String id) throws Exception;
		//대화방 조회
		public ChatRoom select(ChatRoom chatRoom) throws Exception;
		//대화방 목록 조회
		public List<ChatRoom> selectAll() throws Exception;
		//대화방 번호 조회
		public int selectNo() throws Exception;
		//대화방 삭제
		public int delete(ChatRoom chatRoom) throws Exception;
		//대화방 시퀀스 생성
		public int test() throws Exception;
		
		public int updateUserCount(ChatRoom chatRoom) throws Exception;
}
