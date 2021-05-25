package com.withTalk.server.service;

import java.util.List;

import com.withTalk.server.model.Member;

public interface MemberService {
	// 아이디 찾기
	public List<Member> findId(Member member) throws Exception;

	// 비밀번호 찾기
	public Member findPassword(Member member) throws Exception;

	// 사용자 정보 등록
	public void registerMemberInfo(Member member) throws Exception;

	// 사용자 정보 목록 조회
	public List<Member> searchMemberInfoList(Member member) throws Exception;

	// 사용자 정보 조회
	public Member searchMemberInfo(Member member) throws Exception;

	// 사용자 정보 수정
	public void modifyMemberInfo(Member member) throws Exception;
}
