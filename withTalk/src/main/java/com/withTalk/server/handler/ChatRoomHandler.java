package com.withTalk.server.handler;

import java.util.ArrayList;
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
				} else {
					status = "r400";
				}
				
				resultJson.put("type", type);
				resultJson.put("method", method);
				resultJson.put("status", status);
				
				ctx.writeAndFlush(resultJson.toJSONString());
				break;

			// 대화방 조회
			case "select":
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

			// 대화방 검색
			case "search": ////////////////////////////////////
				// To DO

				// 대화방 삭제
			case "delete":

			default:
				System.out.println("not found method...");
			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}
}