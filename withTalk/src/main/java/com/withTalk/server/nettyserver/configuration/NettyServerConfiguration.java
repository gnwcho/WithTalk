package com.withTalk.server.nettyserver.configuration;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.withTalk.server.nettyserver.Initializer.NettyChannelInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Configuration
@PropertySource("classpath:application.properties")
public class NettyServerConfiguration {
	@Value("${server.port}")
	private int PORT;
	
	@Autowired
	NettyChannelInitializer nettyInitializer;
	
	@Bean
	public ServerBootstrap serverBootstrap() {
		ServerBootstrap sb = new ServerBootstrap();
		
		sb.group(parentGroup(), childGroup())
		.channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_BACKLOG, 100)
		.handler(new LoggingHandler(LogLevel.INFO))
		.childHandler(nettyInitializer);
		
		return sb;
	}
	
	@Bean
	public InetSocketAddress port() {
		return new InetSocketAddress(PORT);
	}
	
	@Bean
	public EventLoopGroup parentGroup() {
		return new NioEventLoopGroup(1);
	}
	
	@Bean
	public EventLoopGroup childGroup() {
		return new NioEventLoopGroup();
	}
	
	@Bean
	public JSONParser parser() {
		return new JSONParser();
	}
	
	@Bean
	public Map<String, Channel> mappingMember() {
		return new HashMap<String, Channel>();
	}
	
	@Bean
	public HashMap<Integer, Set<String>> chatRoomMap() {
		return new HashMap<Integer, Set<String>>();
	}
}
