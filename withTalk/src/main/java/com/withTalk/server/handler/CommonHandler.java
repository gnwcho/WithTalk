package com.withTalk.server.handler;

import java.util.Map;

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

@Component
@Sharable
public class CommonHandler extends SimpleChannelInboundHandler<String> {
	@Autowired
	private JSONParser parser;
	@Autowired
	private MemberServiceImpl memberServiceImpl;
	@Autowired
	private Map<String, Channel> mappingMember;
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		try {
			JSONObject jsonObj = (JSONObject) parser.parse(msg);
			String type = (String) jsonObj.get("type");
			
			if ("common".equals(type)) {
				Member member = new Member();
				Member resultMember = null;
				
				JSONObject resultJson = new JSONObject();
				
				String result = null;
				
				String method = (String) jsonObj.get("method");
				String id = null;
				
				switch (method) {
				case "login":
					id = (String) jsonObj.get("id");
					
					member.setId((String) jsonObj.get("id"));
					member.setPassword((String) jsonObj.get("password"));
					
					result = memberServiceImpl.login(member);
					System.out.println(result);
					
					if ("r200".equals(result)) {
						mappingMember.put(id, ctx.channel());
					}
					
					resultJson.put("method", method);
					resultJson.put("status", result);
					
					System.out.println(resultJson.toJSONString());
					
					ctx.writeAndFlush(resultJson.toJSONString());
					break;
					
				default:
					System.out.println("not found method...");
				}
			} else {
				ctx.fireChannelRead(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
