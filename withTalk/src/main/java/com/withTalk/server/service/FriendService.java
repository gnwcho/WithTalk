package com.withTalk.server.service;

import java.util.List;

import com.withTalk.server.model.Friend;
import com.withTalk.server.model.Member;

public interface FriendService {
	//친구 등록
	public int insert(Friend friend);
	//친구 조회
	public int select(Friend friend);
	//친구 찾기
	public Member search(Member member);
	//친구 목록 조회
	public List<Friend> selectAll(Friend friend);
	//친구 삭제
	public int delete(Friend friend);
	//등록된 친구 검색
	public List<Member> selectByName(Member member);
}
