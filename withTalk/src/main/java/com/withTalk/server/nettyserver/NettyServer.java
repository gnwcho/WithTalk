package com.withTalk.server.nettyserver;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;

@Component
public class NettyServer {
	@Autowired
	private ServerBootstrap sb;
	@Autowired
	private InetSocketAddress port;
	
	private ChannelFuture cf;
	
	public void start() throws Exception {
		cf = sb.bind(port).sync();
		cf.channel().closeFuture().sync();
	}
}
