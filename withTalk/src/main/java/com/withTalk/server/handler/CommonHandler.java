package com.withTalk.server.handler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.withTalk.server.model.Member;
import com.withTalk.server.service.MemberServiceImpl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Component
public class CommonHandler extends SimpleChannelInboundHandler<String> {
	@Autowired
	JSONParser parser;
	
	@Autowired
	public MemberServiceImpl memberServiceImpl;
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		JSONObject jsonObj = (JSONObject) parser.parse(msg);
		String type = (String) jsonObj.get("type");
		
		if ("common".equals(type)) {
			Member member = new Member();
			Member resultMember = null;

			JSONObject resultJson = new JSONObject();

			String result = null;

			String method = (String) jsonObj.get("method");

			switch (method) {
				case "login":
					member.setId((String) jsonObj.get("id"));
					member.setPassword((String) jsonObj.get("password"));
					
					result = memberServiceImpl.login(member);
					
					resultJson.put("method", method);
					resultJson.put("status", result);
					
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
