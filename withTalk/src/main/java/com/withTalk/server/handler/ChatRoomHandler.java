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
import com.withTalk.server.nettyserver.NettyServer;
import com.withTalk.server.service.ChatRoomServiceImpl;
import com.withTalk.server.service.JoinChatRoomServiceImpl;

import io.netty.channel.Channel;
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
	private Map<String, Channel> mappingMember;
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
					String chatRoomType = (String) jsonObj.get("chatRoomType");
	
					chatRoom.setType(chatRoomType);
					result = chatRoomServiceImpl.insert(chatRoom, receiverId);
	
					if ((NettyServer.SUCCESS).equals(result)) {
						joinChatRoom.setChatRoomName((String) jsonObj.get("chatRoomName"));
						joinChatRoom.setChatRoomNo(chatRoomServiceImpl.selectNo());
	
						if ("DM".equals(chatRoomType)) {
							status = joinChatRoomServiceImpl.insert(joinChatRoom, receiverId,
									(String) jsonObj.get("senderId"));
						} else {
							status = joinChatRoomServiceImpl.insert(joinChatRoom, receiverId);
						}
	
						resultJson.put("type", type);
						resultJson.put("method", method);
						resultJson.put("status", status);
						resultJson.put("joinMember", receiverId);
						resultJson.put("chatRoomType", chatRoomType);
	
						for (String id : receiverId) {
							Channel ch = mappingMember.get(id);
							if (ch != null) {
								ch.writeAndFlush(resultJson.toJSONString());
							}
						}
	
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
					resultJson.put("status", NettyServer.SUCCESS);
					resultJson.put("chatRoomNo", no);
					resultJson.put("userCount", resultChatRoom.getUserCount());
					resultJson.put("memberIdList", memberIdList);
				} else {
					resultJson.put("status", NettyServer.FAIL);
					resultJson.put("chatRoomNo", null);
					resultJson.put("userCount", null);
					resultJson.put("memberIdList", null);
				}

				ctx.writeAndFlush(resultJson.toJSONString());
				break;

			case "exit":
				resultJson.put("type", type);
				resultJson.put("method", method);

				if (jsonObj.get("chatRoomNo") != null && jsonObj.get("senderId") != null) {
					int roomNo = Integer.parseInt(String.valueOf(jsonObj.get("chatRoomNo")));
					joinChatRoom.setMemberId((String) jsonObj.get("senderId"));
					joinChatRoom.setChatRoomNo(roomNo);

					status = joinChatRoomServiceImpl.delete(joinChatRoom);

					if ((NettyServer.SUCCESS).equals(status)) {
						chatRoom.setSequenceNo(roomNo);

						if (chatRoomServiceImpl.memberCount(chatRoom) == 1) {
							chatRoomServiceImpl.deleteChatRoom(chatRoom);
						} else {
							chatRoomServiceImpl.updateUserCount(chatRoom);
						}
					}

					resultJson.put("status", status);
				} else {
					resultJson.put("status", (NettyServer.FAIL));
				}

				ctx.writeAndFlush(resultJson.toJSONString());
				break;

			// 대화방 검색
			case "search": ////////////////////////////////////
				// To DO
				System.out.println("대화방 검색 들어온 요청 : " + jsonObj);
				joinChatRoom.setChatRoomName((String) jsonObj.get("chatRoomName"));
				joinChatRoom.setMemberId((String) jsonObj.get("senderId"));
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

					resultJson.put("status", NettyServer.SUCCESS);
					resultJson.put("searchChatRoomList", searchJoinChatRoomList);
				} else {
					resultJson.put("status", NettyServer.FAIL);
					resultJson.put("searchChatRoomList", null);
				}

				System.out.println("searchJoinChatRoom 결과 : " + resultJson);
				ctx.writeAndFlush(resultJson.toJSONString());
				break;

			// 삭제 필요
			case "checkExistChatRoom":
				resultJson.put("type", type);
				resultJson.put("method", method);
				resultJson.put("status", NettyServer.FAIL);
				resultJson.put("chatRoomNo", -1);

				System.out.println("checkExChatRoom 결과 : " + resultJson);
				ctx.writeAndFlush(resultJson.toJSONString());
				break;

			// 모든 참여 대화방 조회
			case "selectAllChatRoom":
				String id = (String) jsonObj.get("id");
				joinChatRoom.setMemberId(id);

				List<JoinChatRoom> selectByIdList = joinChatRoomServiceImpl.selectDistinctNo(joinChatRoom);

				resultJson.put("type", type);
				resultJson.put("method", method);

				if (selectByIdList != null) {
					JSONArray chatRoomList = joinChatRoomServiceImpl.selectAllChatRoom(selectByIdList);

					if (chatRoomList != null) {
						resultJson.put("status", NettyServer.SUCCESS);
						resultJson.put("chatRoomList", chatRoomList);
					}
				} else {
					resultJson.put("status", NettyServer.FAIL);
					resultJson.put("chatRoomList", null);
				}

				System.out.println("selectAllChatRoom 결과 : " + resultJson);
				ctx.writeAndFlush(resultJson.toJSONString());
				break;
				
			case "updateName":
				if (jsonObj.get("chatRoomNo") != null && jsonObj.get("newName") != null
						&& jsonObj.get("senderId") != null) {
					int chatRoomNo = Integer.parseInt(String.valueOf(jsonObj.get("chatRoomNo")));
					String chatRoomName = (String) jsonObj.get("newName");
					String memberId = (String) jsonObj.get("senderId");

					joinChatRoom = new JoinChatRoom();

					joinChatRoom.setChatRoomNo(chatRoomNo);
					joinChatRoom.setChatRoomName(chatRoomName);
					joinChatRoom.setMemberId(memberId);

					resultJson.put("type", chatRoom);
					resultJson.put("method", method);

					if (joinChatRoomServiceImpl.update(joinChatRoom) == 1) {
						resultJson.put("status", NettyServer.SUCCESS);
						resultJson.put("chatRoomNo", chatRoomNo);
						resultJson.put("newName", chatRoomName);
						
						System.out.println("updateName 결과 : " + resultJson);
						ctx.writeAndFlush(resultJson.toJSONString());
						break;
					}

				}
					resultJson.put("status", NettyServer.FAIL);
					resultJson.put("chatRoomNo", -1);
					resultJson.put("newName", null);
				
				System.out.println("updateName 결과 : " + resultJson);
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