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
		//대화방 시퀀스 생성
		public int test() throws Exception;
		//대화방 참여자 수 수정
		public void updateUserCount(ChatRoom chatRoom) throws Exception;
		//대화방 참여자 수 조회
		public int memberCount(ChatRoom chatRoom) throws Exception;
		//개인 대화방 존재 확인
		public boolean selectExistDm(ChatRoom chatRoom, List<String> receiverId) throws Exception;
		//대화방 삭제
		public int deleteChatRoom(ChatRoom chatRoom) throws Exception;
}
