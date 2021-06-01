package com.withTalk.server.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.withTalk.server.model.ChatRoom;
import com.withTalk.server.model.JoinChatRoom;

@Mapper
public interface JoinChatRoomMapper {
	//참여 조회
	//public JoinChatRoom select (JoinChatRoom joinChatRoom) throws Exception;
	//참여 삽입
	public int insert (JoinChatRoom joinChatRoom) throws Exception;
	//참여 삭제
	public int delete (JoinChatRoom joinChatRoom) throws Exception;
	//방 id에 속한 회원 찾기
	public List<String> selectMemberByRoomId(ChatRoom chatRoom) throws Exception;
	//참여 조회
	public List<JoinChatRoom> select(JoinChatRoom joinChatRoom) throws Exception;
	//참여 중복 제거 조회
	public List<JoinChatRoom> selectDistinctNo (JoinChatRoom joinChatRoom) throws Exception;
}
