package com.withTalk.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.withTalk.server.model.JoinChatRoom;
import com.withTalk.server.repository.JoinChatRoomMapper;

@Service
public class JoinChatRoomServiceImpl implements JoinChatRoomService {
	@Autowired
	JoinChatRoomServiceImpl joinChatRoomServiceImpl;
	@Autowired
	JoinChatRoomMapper joinChatRoomMapper;
	@Autowired
	public Map<Integer, Set<String>> chatRoomMap;

	// 참여 조회
	@Override
	public List<JoinChatRoom> select(JoinChatRoom joinChatRoom) throws Exception {
		return joinChatRoomMapper.select(joinChatRoom);
	}

	// 단체방 참여 등록
	@Override
	public String insert(JoinChatRoom joinChatRoom, List<String> receiverId) throws Exception {
		try {
			for (String id : receiverId) {
				joinChatRoom.setMemberId(id);
				joinChatRoomMapper.insert(joinChatRoom);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "r400";
		}

		return "r200";
	}

	// 개인방 참여 등록
	@Override
	public String insert(JoinChatRoom joinChatRoom, List<String> receiverId, String senderId) throws Exception {
		try {
			for (String id : receiverId) {
				if (id.equals(senderId)) {
					joinChatRoom.setChatRoomName(receiverId.get(1));
					joinChatRoom.setMemberId(id);
					
					joinChatRoomMapper.insert(joinChatRoom);
				} else {
					joinChatRoom.setChatRoomName(senderId);
					joinChatRoom.setMemberId(id);
					
					joinChatRoomMapper.insert(joinChatRoom);
				}
			}
		} catch (Exception e) {
			return "r400";
		}

		return "r200";
	}
	
	//참여 대화방 이름 변경
	public int update (JoinChatRoom joinChatRoom) throws Exception {
		return joinChatRoomMapper.update(joinChatRoom);
	}

	// 참여 삭제
	@Override
	public String delete(JoinChatRoom joinChatRoom) throws Exception {
		int result = joinChatRoomMapper.delete(joinChatRoom);
		String status = null;

		if (result == 1) {
			Set<String> joinMember = chatRoomMap.get(joinChatRoom.getChatRoomNo());
			joinMember.remove(joinChatRoom.getMemberId());

			status = "r200";
		} else {
			status = "r400";
		}

		return status;
	}

	@Override
	public List<JoinChatRoom> selectDistinctNo(JoinChatRoom joinChatRoom) throws Exception {
		return joinChatRoomMapper.selectDistinctNo(joinChatRoom);
	}

	// 모든 참여 대화방 조회
	@Override
	public JSONArray selectAllChatRoom(List<JoinChatRoom> joinChatRoomList) throws Exception {
		List<JoinChatRoom> selectByIdList = joinChatRoomList;
		JSONArray chatRoomList = new JSONArray();
		JSONObject row = null;
		List<String> idList = null;

		if (selectByIdList != null) {
			for (int i = 0; i < selectByIdList.size(); i++) {
				int chatRoomNo = selectByIdList.get(i).getChatRoomNo();
				JoinChatRoom joinChatRoom = new JoinChatRoom();
				joinChatRoom.setChatRoomNo(chatRoomNo);

				List<JoinChatRoom> selectByNo = joinChatRoomServiceImpl.select(joinChatRoom);
				String chatRoomName = selectByNo.get(0).getChatRoomName();
				System.out.println("chatRoomName : "  + chatRoomName);
				idList = new ArrayList<String>();

				for (int j = 0; j < selectByNo.size(); j++) {
					idList.add(selectByNo.get(j).getMemberId());
				}

				row = new JSONObject();
				row.put("chatRoomNo", chatRoomNo);
				row.put("chatRoomName", chatRoomName);
				row.put("memberIdList", idList);

				if (idList.size() > 2) {
					row.put("chatRoomType", "GM");
				} else {
					row.put("chatRoomType", "DM");
				}

				chatRoomList.add(row);
			}
			
			return chatRoomList;
		}
		
		else {
			return null;
		}
	}
}