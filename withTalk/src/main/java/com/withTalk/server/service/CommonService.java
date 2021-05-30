package com.withTalk.server.service;

import com.withTalk.server.model.Member;

import io.netty.channel.ChannelHandlerContext;

public interface CommonService {
	//로그인
	public String login(Member member, ChannelHandlerContext channel) throws Exception;
	
	//로그아웃
	public String logout(Member member, ChannelHandlerContext ctx) throws Exception;
}
