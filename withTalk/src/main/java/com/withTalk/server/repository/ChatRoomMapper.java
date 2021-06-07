package com.withTalk.server.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.withTalk.server.model.ChatRoom;
import com.withTalk.server.model.Member;

@Mapper
public interface ChatRoomMapper {
	//대화방 생성
	public int insert(ChatRoom chatRoom) throws Exception;
	//대화방 조회
	public ChatRoom select(ChatRoom chatRoom) throws Exception;
	//대화방 목록 조회
	public List<ChatRoom> selectAll() throws Exception;
	//대화방 번호 조회
	public int selectNo() throws Exception;
	//대화방 삭제
	public int delete(ChatRoom chatRoom) throws Exception;
	//대화방 인원 수정
	public void updateCount(ChatRoom chatRoom) throws Exception;
	//대화방 인원 조회
	public int selectCount(ChatRoom chatRoom) throws Exception;
}
