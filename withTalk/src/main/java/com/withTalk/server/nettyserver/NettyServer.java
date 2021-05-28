package com.withTalk.server.nettyserver;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.withTalk.server.repository.MemberMapper;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

@Component
public class NettyServer {
	@Autowired
	private ServerBootstrap sb;
	@Autowired
	private InetSocketAddress port;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private Map<String, Channel> mappingMember;
	
	private ChannelFuture cf;
	
	public void start() throws Exception {
		List<String> listId = new ArrayList<>();
		
		listId = memberMapper.selectAllId();
		
		cf = sb.bind(port).sync();
		cf.channel().closeFuture().sync();
	}
}
