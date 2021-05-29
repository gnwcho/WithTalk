package com.withTalk.server.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.withTalk.server.model.ChatRoom;
import com.withTalk.server.model.Member;
import com.withTalk.server.repository.ChatRoomMapper;

import io.netty.channel.Channel;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {
	@Autowired
	public ChatRoomMapper chatRoomMapper;
	@Autowired
	public Map<String, Channel> idMap;
	@Autowired
	public Map<Integer, Set<String>> chatRoomMap;
	
	//대화방 생성
	@Override
	public int insert(ChatRoom chatRoom) throws Exception{
		int result = chatRoomMapper.insert(chatRoom);
		
		return result;
	}
	
	//대화방Map에 등록
	@Override
	public void registerMap(int roomId, String id) throws Exception {
		Set idSet = new HashSet();
		idSet.add(id);
		chatRoomMap.put(roomId, idSet);
	}

	//대화방 조회
	@Override
	public ChatRoom select(ChatRoom chatRoom) throws Exception {
		ChatRoom row = chatRoomMapper.select(chatRoom);
		
		
		return null;
	}

	@Override
	public List<ChatRoom> selectAll() throws Exception {
		return null;
	}

	@Override
	public int delete(ChatRoom chatRoom) throws Exception {
		return 0;
	}

	@Override
	public int selectNo() throws Exception {
		int no = chatRoomMapper.selectNo();
		return no;
	}

	@Override
	public int test() throws Exception {
		return chatRoomMapper.test();
		
	}
}
