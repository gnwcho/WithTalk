package com.withTalk.server.nettyserver;

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
public class JsonHandler extends SimpleChannelInboundHandler<String> {
	@Autowired 
	public JSONParser parser;
	
	@Autowired
	public MemberServiceImpl memberServiceImpl;
	@Autowired
	public FriendServiceImpl friendServiceImpl;
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		JSONObject jsonObj = (JSONObject) parser.parse(msg);
		System.out.println("msg : " + msg);
		String method = (String) jsonObj.get("method");
		
		Member member = new Member();
		Member resultMember = null;
		
		Friend friend = new Friend();
		Friend resultFriend = null;
		
		switch (method) {
			case "findId" :
				member.setName((String)jsonObj.get("name"));
				member.setPhoneNo((String)jsonObj.get("phoneNo"));
				
				resultMember = memberServiceImpl.searchMemberInfo(member);
				
				ctx.writeAndFlush(resultMember.getId());
				break;
				
			case "findPassword" :
				member.setId((String)jsonObj.get("id"));
				member.setName((String)jsonObj.get("name"));
				
				resultMember = memberServiceImpl.searchMemberInfo(member);
				
				ctx.writeAndFlush(resultMember.getPassword());
				break;
				
			case "insertFriend" :
				friend.setMemberId((String)jsonObj.get("memberId"));
				friend.setFriendId((String)jsonObj.get("friendId"));
				
				System.out.println("insertFriend: " + friend);
				
				int result = friendServiceImpl.insert(friend);
				
				System.out.println("insertFriend 결과 : " + result);
				
				ctx.writeAndFlush(result);
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
				
			default :
				System.out.println("그 외의 메소드");
		}
	}
	
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
    }
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}