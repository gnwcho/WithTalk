package com.withTalk.server.handler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.withTalk.server.model.Friend;
import com.withTalk.server.model.Member;
import com.withTalk.server.service.FriendServiceImpl;
import com.withTalk.server.service.MemberServiceImpl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
@Component
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
				case "findId":
					member.setName((String)jsonObj.get("name"));
					member.setPhoneNo((String)jsonObj.get("phoneNo"));
					
					resultMember = memberServiceImpl.searchMemberInfo(member);
					
					ctx.writeAndFlush(resultMember.getId());
					break;
					
				case "findPassword":
					member.setId((String)jsonObj.get("id"));
					member.setName((String)jsonObj.get("name"));
					
					resultMember = memberServiceImpl.searchMemberInfo(member);
					
					ctx.writeAndFlush(resultMember.getPassword());
					break;
					
				case "insertFriend" :
					friend.setMemberId((String)jsonObj.get("memberId"));
					friend.setFriendId((String)jsonObj.get("friendId"));
					
					System.out.println("insertFriend: " + friend);
					
					/*result = friendServiceImpl.insert(friend);
					
					System.out.println("insertFriend 결과 : " + result);
					
					ctx.writeAndFlush(result);*/
					break;
	
				case "searchFriend" :
					member.setId((String)jsonObj.get("id"));
					
					resultMember = friendServiceImpl.selectByName(member);
					System.out.println("결과 : " + resultMember);
					
					ctx.writeAndFlush(resultMember.getName());
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
