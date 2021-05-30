package com.withTalk.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.withTalk.server.model.JoinChatRoom;
import com.withTalk.server.repository.JoinChatRoomMapper;

@Service
public class JoinChatRoomServiceImpl implements JoinChatRoomService {
	@Autowired
	JoinChatRoomMapper joinChatRoomMapper;
	
	//참여 조회
	@Override
	public List<JoinChatRoom> select(JoinChatRoom joinChatRoom) throws Exception {
		return null;
	}

	//참여 등록
	@Override
	public String insert(JoinChatRoom joinChatRoom, List<String> receiverId) throws Exception {
		try {
			for (String id : receiverId) {
				joinChatRoom.setMemberId(id);
				
				joinChatRoomMapper.insert(joinChatRoom);
			}
		} catch (Exception e) {
			return "r400";
		}
		return "r200";
	}

	//참여 삭제
	@Override
	public void delete(JoinChatRoom joinChatRoom) throws Exception {
		// TODO Auto-generated method stub

	}

}
