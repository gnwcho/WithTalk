package com.withTalk.server.handler;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
@Component
public class ChatHandler  extends SimpleChannelInboundHandler<String> {

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
