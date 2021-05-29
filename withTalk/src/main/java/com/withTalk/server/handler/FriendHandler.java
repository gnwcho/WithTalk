package com.withTalk.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.withTalk.server.model.Friend;
import com.withTalk.server.model.Member;
import com.withTalk.server.service.FriendServiceImpl;
import com.withTalk.server.service.MemberServiceImpl;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Component
@Sharable
public class FriendHandler extends SimpleChannelInboundHandler<String> {
	@Autowired
	JSONParser parser;
	@Autowired
	public MemberServiceImpl memberServiceImpl;
	@Autowired
	public FriendServiceImpl friendServiceImpl;
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		JSONObject jsonObj = (JSONObject) parser.parse(msg);
		String type = (String) jsonObj.get("type");
		
		if ("friend".equals(type)) {
			Member member = new Member();
			Member resultMember = null;
			
			Friend friend = new Friend();
			Friend resultFriend = null;

			JSONObject resultJson = new JSONObject();

			String result = null;

			String method = (String) jsonObj.get("method");

			switch (method) {
				case "insertFriend" :
					friend.setMemberId((String)jsonObj.get("memberId"));
					friend.setFriendId((String)jsonObj.get("friendId"));
					
					int insertResult = friendServiceImpl.insert(friend);
					
					resultJson.put("type", type);
					resultJson.put("method", method);
					if (insertResult == 0) {
						resultJson.put("result", "r400");
					} else {
						resultJson.put("result", "r200");
					}
					
					ctx.writeAndFlush(resultJson.toJSONString());
					break;
	
				case "searchFriend" :
					member.setPhoneNo((String)jsonObj.get("phoneNo"));
					
					resultMember = friendServiceImpl.search(member);
					System.out.println("결과 : " + resultMember);
					
					resultJson.put("type", type);
					resultJson.put("method", method);
					resultJson.put("id", resultMember.getId());
					resultJson.put("name", resultMember.getName());
					resultJson.put("phoneNo", resultMember.getPhoneNo());
					
					ctx.writeAndFlush(resultJson.toJSONString());
					break;
					
				case "selectAllFriend" :
					System.out.println("selectAllFriend 들어옴");
					friend.setFriendId((String)jsonObj.get("id"));
					
					List<Friend> friendList = new ArrayList<Friend>();
					friendList = friendServiceImpl.selectAll(friend);
					
					ctx.writeAndFlush(friendList);
					break;
					
				default:
					System.out.println("not found method...");
			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	
	
}
