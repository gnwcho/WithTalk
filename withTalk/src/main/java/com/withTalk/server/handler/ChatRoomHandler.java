package com.withTalk.server.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

			JSONObject resultJson = new JSONObject();

			String method = (String) jsonObj.get("method");
			
			int result = 0;
			int no = 0;

			switch (method) {
				case "create":
					List<String> receiverId = (List<String>) jsonObj.get("receiverId");
					
					Set<String> idSet = new HashSet<String>();
					idSet.add((String)jsonObj.get("senderId"));
					
					for (int i = 0; i < receiverId.size(); i++) {
						idSet.add(receiverId.get(i));
					}
					
					chatRoom.setUserCount(receiverId.size());
					
					chatRoomServiceImpl.test();
					
					no = chatRoomServiceImpl.selectNo();
					
					String chatRoomName = (String) jsonObj.get("chatRoomName");
					
					joinChatRoom.setChatRoomName(chatRoomName);
					joinChatRoom.setChatRoomNo(no + 1);
					
					
					joinChatRoomServiceImpl.insert(joinChatRoom);
					
					if (result == 0) {
						resultJson.put("type", "chatRoom");
						resultJson.put("method", method);
						resultJson.put("roomId", -1);
					} else {
						resultJson.put("type", "chatRoom");
						resultJson.put("method", method);
						resultJson.put("roomId", no + 1);
						
						chatRoomMap.put(no + 1, idSet);
						
						System.out.println(chatRoomMap);
					}
					
					ctx.writeAndFlush(resultJson.toJSONString());
					break;
					
				case "search" :
					no = (int) jsonObj.get("roomId");
					chatRoom.setSequenceNo(no);
					
					resultChatRoom = chatRoomServiceImpl.select(chatRoom);
					
					joinChatRoom.setChatRoomNo(no);
					
					List<JoinChatRoom> resultJoinChatRoomList = joinChatRoomServiceImpl.select(joinChatRoom);
					List<String> memberIdList = new ArrayList<String>();
					
					for (int i = 0; i < resultJoinChatRoomList.size(); i++) {
						memberIdList.add(resultJoinChatRoomList.get(i).getMemberId());
					}
					
					if (resultChatRoom != null && resultJoinChatRoomList != null) {
						resultJson.put("type", "chatRoom");
						resultJson.put("method", method);
						resultJson.put("roomId", no);
						resultJson.put("userCount", resultChatRoom.getUserCount());
						resultJson.put("memberId", memberIdList);
					}
					
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