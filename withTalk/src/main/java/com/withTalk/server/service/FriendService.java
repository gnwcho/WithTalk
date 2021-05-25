package com.withTalk.server.service;

import java.util.List;

import com.withTalk.server.model.Friend;
import com.withTalk.server.model.Member;

public interface FriendService {
	//친구 등록
	public int insert(Friend friend);
	//친구 검색
	public Member selectByName(Member member);
	//친구 목록 조회
	public List<Friend> selectAll(Friend friend);
	//친구 삭제
	public int delete(Friend friend);
}
