package com.withTalk.server.service;

import java.util.List;

import com.withTalk.server.model.Member;

public interface MemberService {
	// 회원 가입
	public String signUp(Member member) throws Exception;
	
	//아이디 체크
	public String checkId(Member member) throws Exception;
	
	//로그인
	public String login(Member member) throws Exception;
	
	// 아이디 찾기
	public List<Member> findId(Member member) throws Exception;

	// 사용자 정보 목록 조회
	public List<Member> searchMemberInfoList(Member member) throws Exception;

	// 사용자 정보 조회
	public Member searchMemberInfo(Member member) throws Exception;

}

