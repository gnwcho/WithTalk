package com.withTalk.server.handler;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.withTalk.server.model.Friend;
import com.withTalk.server.model.Member;
import com.withTalk.server.service.FriendServiceImpl;
import com.withTalk.server.service.MemberServiceImpl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

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
					
					System.out.println("insertFriend: " + friend.getFriendId() + "_" + friend.getMemberId());
					
					int insertResult = friendServiceImpl.insert(friend);
					
					System.out.println("insertFriend 결과 : " + insertResult);
					
					ctx.writeAndFlush(insertResult);
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
					friend.setMemberId((String)jsonObj.get("memberId"));
					
					ctx.writeAndFlush(resultFriend);
					break;
					
				default:
					System.out.println("not found method...");
			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	
	
}
