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
	

	//사용자 정보 조회
	public Member searchMemberInfo(Member member) throws Exception {
		Member row = memberMapper.select(member);
		
		if (row != null) {
			return row;
		} else {
			return null;
		}
	}

	//사용자 정보 수정
	@Override
	public int updateMemberInfo(Member member) throws Exception {
		return memberMapper.update(member);
	}
}
