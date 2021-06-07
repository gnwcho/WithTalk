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
import com.withTalk.server.model.Member;
import com.withTalk.server.model.Message;
import com.withTalk.server.repository.JoinChatRoomMapper;
import com.withTalk.server.repository.MemberMapper;

@Service
public class JoinChatRoomServiceImpl implements JoinChatRoomService {
	@Autowired
	JoinChatRoomServiceImpl joinChatRoomServiceImpl;
	@Autowired
	JoinChatRoomMapper joinChatRoomMapper;
	@Autowired
	MemberMapper memberMapper;
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
					Member member = new Member();
					member.setId(receiverId.get(1));
					member = memberMapper.select(member);
					joinChatRoom.setChatRoomName(member.getName());
					joinChatRoom.setMemberId(id);

					joinChatRoomMapper.insert(joinChatRoom);
				} else {
					Member member = new Member();
					member.setId(senderId);
					member = memberMapper.select(member);
					joinChatRoom.setChatRoomName(member.getName());
					joinChatRoom.setMemberId(id);

					joinChatRoomMapper.insert(joinChatRoom);
				}
			}
		} catch (Exception e) {
			return "r400";
		}

		return "r200";
	}

	// 참여 대화방 이름 변경
	public int update(JoinChatRoom joinChatRoom) throws Exception {
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

	@Override
	// 메시지 형식 반환 id로 검색
	public List<Message> selectById(JoinChatRoom joinChatRoom) throws Exception {
		return joinChatRoomMapper.selectById(joinChatRoom);
	}

	// 모든 참여 대화방 조회
	@Override
	public JSONArray selectAllChatRoom(String id, List<Integer> noList, List<String> timeList) throws Exception {
		JSONArray chatRoomList = new JSONArray();
		JSONObject row = null;
		List<String> idList = null;
		String chatRoomName = null;

		for (int i = 0; i < noList.size(); i++) {
			int chatRoomNo = noList.get(i);
			String sendTime = timeList.get(i);

			JoinChatRoom joinChatRoom = new JoinChatRoom();
			joinChatRoom.setChatRoomNo(chatRoomNo);

			List<JoinChatRoom> selectByNo = joinChatRoomServiceImpl.select(joinChatRoom);

			idList = new ArrayList<String>();

			for (int j = 0; j < selectByNo.size(); j++) {
				idList.add(selectByNo.get(j).getMemberId());
				if ((selectByNo.get(j).getMemberId()).equals(id)) {
					chatRoomName = selectByNo.get(j).getChatRoomName();

					row = new JSONObject();
					row.put("chatRoomNo", chatRoomNo);
					row.put("chatRoomName", chatRoomName);
					row.put("memberIdList", idList);
					row.put("sendTime", sendTime);

					if (idList.size() > 2) {
						row.put("chatRoomType", "GM");
					} else {
						row.put("chatRoomType", "DM");
					}

					chatRoomList.add(row);
				}
			}
		}
		
		return chatRoomList;
	}
}