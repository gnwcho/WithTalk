package com.withTalk.server.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.withTalk.server.model.ChatRoom;
import com.withTalk.server.model.JoinChatRoom;
import com.withTalk.server.service.ChatRoomServiceImpl;
import com.withTalk.server.service.JoinChatRoomServiceImpl;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
@Component
public class ChatRoomHandler extends SimpleChannelInboundHandler<String> {
	@Autowired
	JSONParser parser;
	@Autowired
	public ChatRoomServiceImpl chatRoomServiceImpl;
	@Autowired
	public JoinChatRoomServiceImpl joinChatRoomServiceImpl;
	@Autowired
	public Map<Integer, Set<String>> chatRoomMap;

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		JSONObject jsonObj = (JSONObject) parser.parse(msg);
		String type = (String) jsonObj.get("type");

		if ("chatRoom".equals(type)) {
			ChatRoom chatRoom = new ChatRoom();
			ChatRoom resultChatRoom = null;

			JoinChatRoom joinChatRoom = new JoinChatRoom();
			List<JoinChatRoom> resultJoinChatRoomList = null;

			JSONObject resultJson = new JSONObject();

			String method = (String) jsonObj.get("method");

			String result = null;
			String status = null;
			int no = 0;

			switch (method) {
			// 대화방 생성
			case "create":
				
				List<String> receiverId = (List<String>) jsonObj.get("receiverId");

				result = chatRoomServiceImpl.insert(chatRoom, receiverId);

				if ("r200".equals(result)) {
					joinChatRoom.setChatRoomName((String) jsonObj.get("chatRoomName"));
					joinChatRoom.setChatRoomNo(chatRoomServiceImpl.selectNo());

					if ("r200".equals(joinChatRoomServiceImpl.insert(joinChatRoom, receiverId))) {
						status = "r200";
					} else {
						status = "r400";
					}
					resultJson.put("type", type);
					resultJson.put("method", method);
					resultJson.put("status", status);

					System.out.println("create 메소드 출력하는 메세지 : " + resultJson);
					ctx.writeAndFlush(resultJson.toJSONString());
					break;
				}

				// 대화방 조회
			case "select":
				no = (int) jsonObj.get("chatRoomNo");
				chatRoom.setSequenceNo(no);

				resultChatRoom = chatRoomServiceImpl.select(chatRoom);

				joinChatRoom.setChatRoomNo(no);

				resultJoinChatRoomList = joinChatRoomServiceImpl.select(joinChatRoom);
				List<String> memberIdList = new ArrayList<String>();

				for (int i = 0; i < resultJoinChatRoomList.size(); i++) {
					memberIdList.add(resultJoinChatRoomList.get(i).getMemberId());
				}

				resultJson.put("type", type);
				resultJson.put("method", method);
				
				if (resultChatRoom != null && resultJoinChatRoomList != null) {
					resultJson.put("status", "r200");
					resultJson.put("chatRoomNo", no);
					resultJson.put("userCount", resultChatRoom.getUserCount());
					resultJson.put("memberId", memberIdList);
				} else {
					resultJson.put("status","r400");
					resultJson.put("chatRoomNo", null);
					resultJson.put("userCount", null);
					resultJson.put("memberId", null);
				}

				ctx.writeAndFlush(resultJson.toJSONString());
				break;

			
// 대화방 나가기
			case "exit":
				int roomNo = Integer.parseInt(String.valueOf(jsonObj.get("chatRoomNo")));
				
				joinChatRoom.setMemberId((String) jsonObj.get("senderId"));
				joinChatRoom.setChatRoomNo(roomNo);
				
				status = joinChatRoomServiceImpl.delete(joinChatRoom);
				
				if ("r200".equals(status)) {
					chatRoom.setSequenceNo(roomNo);
					
					if (chatRoomServiceImpl.memberCount(chatRoom) == 1) {
						chatRoomServiceImpl.deleteChatRoom(chatRoom);
					} else {
						chatRoomServiceImpl.updateUserCount(chatRoom);
					}
				}
				
				resultJson.put("type", type);
				resultJson.put("method", method);
				resultJson.put("status", status);
				
				ctx.writeAndFlush(resultJson.toJSONString());
				break;
				
				// 대화방 검색
				case "search": ////////////////////////////////////
				// To DO
				System.out.println("대화방 검색 들어온 요청 : " + jsonObj);
				joinChatRoom.setChatRoomName((String)jsonObj.get("chatRoomName"));
				joinChatRoom.setMemberId((String)jsonObj.get("senderId"));
				resultJoinChatRoomList = joinChatRoomServiceImpl.select(joinChatRoom);
				System.out.println("resultJoinChatRoomList 결과 : " + resultJoinChatRoomList);
				
				List<JSONObject> searchJoinChatRoomList = new ArrayList<JSONObject>();
				JSONObject searchJoinChatRoom = null;
				
				resultJson.put("type", type);
				resultJson.put("method", method);
				
				if (resultJoinChatRoomList != null) {
					for (int i = 0; i < resultJoinChatRoomList.size(); i++) {
						searchJoinChatRoom = new JSONObject();
						
						searchJoinChatRoom.put("chatRoomNo", resultJoinChatRoomList.get(i).getChatRoomNo());
						searchJoinChatRoom.put("chatRoomName", resultJoinChatRoomList.get(i).getChatRoomName());
						
						searchJoinChatRoomList.add(searchJoinChatRoom);
					}
					
					resultJson.put("status","r200");
					resultJson.put("searchChatRoomList", searchJoinChatRoomList);
				} else {
					resultJson.put("status", "r400");
					resultJson.put("searchChatRoomList", null);
				}
				
				System.out.println("searchJoinChatRoom 결과 : " + resultJson);
				ctx.writeAndFlush(resultJson.toJSONString());
				break;
				
			case "check":
				resultJson.put("type", type);
				resultJson.put("method", method);
				resultJson.put("status", "r400");
				resultJson.put("chatRoomNo", -1);
				
				System.out.println("checkExChatRoom 결과 : " + resultJson);
				ctx.writeAndFlush(resultJson.toJSONString());
				
				break;

			default:
				System.out.println("not found method...");
			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}
}