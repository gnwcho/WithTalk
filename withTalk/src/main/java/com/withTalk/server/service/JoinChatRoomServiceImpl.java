package com.withTalk.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.withTalk.server.model.JoinChatRoom;

@Service
public class JoinChatRoomServiceImpl implements JoinChatRoomService {
	//참여 조회
	@Override
	public List<JoinChatRoom> select(JoinChatRoom joinChatRoom) throws Exception {
		return null;
	}

	//참여 등록
	@Override
	public void insert(JoinChatRoom joinChatRoom) throws Exception {
		// TODO Auto-generated method stub

	}

	//참여 삭제
	@Override
	public void delete(JoinChatRoom joinChatRoom) throws Exception {
		// TODO Auto-generated method stub

	}

}
