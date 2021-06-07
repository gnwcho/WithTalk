package com.withTalk.server.handler;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.withTalk.server.model.Message;
import com.withTalk.server.service.ChatServiceImpl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
@Component
public class ChatHandler  extends SimpleChannelInboundHandler<String> {
	@Autowired
	JSONParser parser;
	@Autowired
	ChatServiceImpl chatServiceImpl;
	@Autowired
	private Map<String, Channel> mappingMember;
	@Autowired
	public Map<Integer, Set<String>> chatRoomMap;
	
	public static final String SUCCESS = "r200";
	public static final String FAIL = "r400";
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		JSONObject jsonObj = (JSONObject) parser.parse(msg);
		String type = (String) jsonObj.get("type");
		
		if ("chat".equals(type)) {
			
			String method = (String) jsonObj.get("method");
			Message message = new Message();
			
			JSONObject resultJson = new JSONObject();
			
			switch (method) {
			case "sendChat":
				message.setContents((String) jsonObj.get("contents"));
				message.setSenderId((String) jsonObj.get("senderId"));
				message.setChatRoomNo(Integer.parseInt(String.valueOf(jsonObj.get("chatRoomNo"))));
				
				message = chatServiceImpl.sendMessage(message);
				if (message.getSequenceNo() != 0) {
					int chatRoomNo = message.getChatRoomNo();
					Set<String> joinMember = chatRoomMap.get(chatRoomNo);
					
					Iterator<String> it = joinMember.iterator();
					
					while (it.hasNext()) {
						String receiveId = it.next();
						if (mappingMember.get(receiveId) != null) {
							resultJson.put("type", type);
							resultJson.put("method", method);
							resultJson.put("seqNo", message.getSequenceNo());
							resultJson.put("chatRoomNo", chatRoomNo);
							resultJson.put("contents", message.getContents());
							resultJson.put("senderId", message.getSenderId());
							resultJson.put("sendTime", message.getSendTime());
							
							mappingMember.get(receiveId).writeAndFlush(resultJson.toJSONString());
						}
					}
				} else {
					ctx.writeAndFlush("don't send Message");
				}
				
				break;
			default:
			}
			
		} else {
			
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().close();
		
	}
	
	@Override
	  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	    cause.printStackTrace();
	    ctx.close();
	  }

}
