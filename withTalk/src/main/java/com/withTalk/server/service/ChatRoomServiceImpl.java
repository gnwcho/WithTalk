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
	public Map<String, Channel> mappingMapper;
	@Autowired
	public Map<Integer, Set<String>> chatRoomMap;
	
	//대화방 생성
	@Override
	public String insert(ChatRoom chatRoom, List<String> receiverId) throws Exception{
		chatRoom.setUserCount(receiverId.size());
		
		int insertResult = chatRoomMapper.insert(chatRoom);
		
		String result = null;
				
		if (insertResult == 1) {
			Set<String> receiverIdSet = new HashSet<String>();

			for (String id : receiverId) {
				receiverIdSet.add(id);
			}
			
			int no = this.selectNo();
			chatRoomMap.put(no, receiverIdSet);
			System.out.println(no);
			
			result = "r200";
		} else {
			result = "r400";
		}
		
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
	public int deleteChatRoom(ChatRoom chatRoom) throws Exception {
		return chatRoomMapper.delete(chatRoom);
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
	
	//대화방 인원 변경
	@Override
	public void updateUserCount(ChatRoom chatRoom) throws Exception {
		chatRoomMapper.updateCount(chatRoom);
	}
	
	//대화방 인원 조회
	@Override
	public int memberCount(ChatRoom chatRoom) throws Exception {
		int result = chatRoomMapper.selectCount(chatRoom);
		
		return result;
	}


	public boolean selectExistDm(ChatRoom chatRoom, List<String> receiverId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
}
