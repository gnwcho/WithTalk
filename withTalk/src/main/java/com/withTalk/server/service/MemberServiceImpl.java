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
	
	@Override
	public String signUp(Member member) throws Exception {
		String result = null;
		
		if (memberMapper.select(member) == null) {
			if(memberMapper.insert(member) == 1) {
				result = "r200";
			}
		} else {
			result = "r400";
		}
		
		return result;
	}
	
	@Override
	public String login(Member member) throws Exception {
		String result = null;
		
		//로그인 성공
		if (memberMapper.select(member) != null) {
			result = "r200";
		} else {
			result = "r400";
		}
		
		return result;
	}

	@Override
	public String checkId(Member member) throws Exception {
		String result = null;
		
		//중복된 아이디 있을 경우
		if (memberMapper.select(member) == null) {
			result = "r200";
		} else {
			result = "r400";
		}
		
		return result;
	}
	
	public List<Member> findId(Member member) throws Exception {

		return null;
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
