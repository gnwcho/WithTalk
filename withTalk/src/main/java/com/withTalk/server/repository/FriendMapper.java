package com.withTalk.server.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.withTalk.server.model.Friend;
import com.withTalk.server.model.Member;

@Mapper
public interface FriendMapper {
	public int insert(Friend friend);

	public List<Friend> selectAll(Friend friend) throws Exception;
	
	public Member select(Member member) throws Exception;

	public int delete(Friend friend) throws Exception;
	
	public int plusSeq() throws Exception;
	
	public List<Member> selectByName(Member member) throws Exception;
}
