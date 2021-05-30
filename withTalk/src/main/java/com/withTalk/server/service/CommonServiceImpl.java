package com.withTalk.server.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.withTalk.server.model.Member;
import com.withTalk.server.repository.MemberMapper;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

@Service
public class CommonServiceImpl implements CommonService {
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private Map<String, Channel> mappingMember;
	
	@Override
	public String login(Member member, ChannelHandlerContext ctx) throws Exception {
		String result = null;
		
		//로그인 성공
		if (memberMapper.select(member) != null) {
			mappingMember.put(member.getId(), ctx.channel());
			System.out.println(mappingMember.get(member.getId()));
			result = "r200";
		} else {
			result = "r400";
		}
		
		return result;
	}

	@Override
	public String logout(Member member, ChannelHandlerContext ctx) throws Exception {
		String result = null;
		String id = member.getId();
		
		if (mappingMember.get(id) != null) {
			mappingMember.put(id, null);
			System.out.println(mappingMember.get(id));
			result = "r200";
		} else {
			result = "r400";
		}
		
		return result;
	}
	
}
