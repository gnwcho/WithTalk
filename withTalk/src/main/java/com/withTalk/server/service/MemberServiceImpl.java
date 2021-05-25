package com.withTalk.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.withTalk.server.model.Member;
import com.withTalk.server.repository.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	public MemberMapper memberMapper;

	public List<Member> findId(Member member) throws Exception {

		return null;
	}

	public Member findPassword(Member member) throws Exception {
		return null;
	}

	public void registerMemberInfo(Member member) throws Exception {

	}

	public List<Member> searchMemberInfoList(Member member) throws Exception {
		return null;
	}

	public Member searchMemberInfo(Member member) throws Exception {
		Member row = memberMapper.select(member);

		if (row != null) {
			return row;
		} else {
			return null;
		}
	}

	public void modifyMemberInfo(Member member) throws Exception {

	}

}
