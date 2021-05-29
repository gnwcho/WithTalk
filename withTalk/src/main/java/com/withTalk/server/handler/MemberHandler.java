package com.withTalk.server.handler;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.withTalk.server.model.Member;
import com.withTalk.server.service.MemberServiceImpl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

@Component
@Sharable
public class MemberHandler extends SimpleChannelInboundHandler<String> {
	@Autowired
	private JSONParser parser;
	@Autowired
	private MemberServiceImpl memberServiceImpl;
	@Autowired
	private Map<String, Channel> mappingMember;
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println("------------------------------------------------------------------------" + msg);
		JSONObject jsonObj = (JSONObject) parser.parse(msg);
		String type = (String) jsonObj.get("type");

		if ("member".equals(type)) {
			Member member = new Member();
			Member resultMember = null;

			JSONObject resultJson = new JSONObject();

			String result = null;

			String method = (String) jsonObj.get("method");

			switch (method) {
				case "auth" :
					member.setId((String)jsonObj.get("id"));
					member.setName((String)jsonObj.get("name"));
					member.setPhoneNo((String)jsonObj.get("phoneNo"));
					
					resultMember = memberServiceImpl.searchMemberInfo(member);
					
					resultJson.put("type", type);
					resultJson.put("method", method);
					
					if (resultMember != null) {
						resultJson.put("status", "r200");
					} else {
						resultJson.put("status", "r400");
					}
					
					ctx.writeAndFlush(resultJson.toJSONString());
					break;
					
				case "findId" :
					member.setName((String)jsonObj.get("name"));
					member.setPhoneNo((String)jsonObj.get("phoneNo"));
					
					resultMember = memberServiceImpl.searchMemberInfo(member);
					
					resultJson.put("type", "member");
					resultJson.put("method", method);
					
					if (resultMember != null) {
						resultJson.put("id", resultMember.getId());
						resultJson.put("status", "r200");
					} else {
						resultJson.put("id", null);
						resultJson.put("status", "r400");
					}
					
					ctx.writeAndFlush(resultJson.toJSONString());
					break;
					
				case "findPassword" :
					member.setId((String)jsonObj.get("id"));
					member.setName((String)jsonObj.get("name"));
					
					resultMember = memberServiceImpl.searchMemberInfo(member);
					
					ctx.writeAndFlush(resultMember.getPassword());
					break;	
					
				case "signUp":
					String id = (String) jsonObj.get("id");
					member.setId((String) jsonObj.get("id"));
					member.setName((String) jsonObj.get("name"));
					member.setPassword((String) jsonObj.get("password"));
					member.setPhoneNo((String) jsonObj.get("phoneNo"));
	
					result = memberServiceImpl.signUp(member);
					
					if ("r200".equals(result)) {
						mappingMember.put(id, null);
					}
					
					resultJson.put("method", method);
					resultJson.put("status", result);
					
					System.out.println(resultJson.toJSONString());
	
					ctx.writeAndFlush(resultJson.toJSONString());
					break;
					
				case "checkId":
					member.setId((String) jsonObj.get("id"));
					
					result = memberServiceImpl.checkId(member);
					
					resultJson.put("method", method);
					resultJson.put("status", result);
					
					ctx.writeAndFlush(resultJson.toJSONString());
					break;
	
				case "resetPassword" :
					System.out.println("들어오냐?");
					member.setId((String) jsonObj.get("id"));
					member.setPassword((String) jsonObj.get("newPassword"));
					
					System.out.println("멤버 : " + member);
					
					int resetResult = memberServiceImpl.updateMemberInfo(member);
					System.out.println("resetResult = " + resetResult);
					
					resultJson.put("method", method);
					resultJson.put("status", result);
					
					if (resetResult == 0) {
						resultJson.put("status", "r400");
					} else {
						resultJson.put("status", "r200");
					}
					
					System.out.println("resultJson : " + resultJson.toJSONString());
					
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
