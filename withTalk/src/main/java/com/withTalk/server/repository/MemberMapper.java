package com.withTalk.server.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.withTalk.server.model.Member;

@Mapper
public interface MemberMapper {
	public int insert(Member member) throws Exception;

	public List<Member> selectAll(Member member) throws Exception;
	
	public List<Member> selectAllById(Member member) throws Exception;

	public Member select(Member member) throws Exception;

	public int update(Member member) throws Exception;
}