package com.withTalk.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.withTalk.server.model.Friend;
import com.withTalk.server.model.Member;
import com.withTalk.server.repository.FriendMapper;

@Service
public class FriendServiceImpl implements FriendService{
	@Autowired
	public FriendMapper friendMapper;
	
	//친구 등록
	@Transactional
	@Override
	public int insert(Friend friend) {
		try {
			int result = friendMapper.insert(friend);
			
			return result;
		} catch (Exception e) {
			return 0;
		}
		
	}
	//친구 검색
	@Override
	public Member selectByName(Member member) {
		try {
			System.out.println("넘어온 Member : " + member);
			Member row = friendMapper.selectByName(member);
			System.out.println("selectMyName 결과 : " + row);
			return row;
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
		
	}
	//친구 목록 조회
	@Override
	public List<Friend> selectAll(Friend friend) {
		try {
			List<Friend> rows = friendMapper.selectAll(friend);
			
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}
	//친구 삭제
	@Transactional
	@Override
	public int delete(Friend friend) {
		try {
			int result = friendMapper.delete(friend);
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			
			return 0;
		}
	}
}
