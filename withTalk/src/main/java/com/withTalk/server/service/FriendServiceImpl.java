package com.withTalk.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.withTalk.server.model.Friend;
import com.withTalk.server.model.Member;
import com.withTalk.server.repository.FriendMapper;
import com.withTalk.server.repository.MemberMapper;

@Service
public class FriendServiceImpl implements FriendService{
	@Autowired
	public FriendMapper friendMapper;
	@Autowired
	public MemberMapper memberMapper;
	
	//친구 등록
	@Transactional
	@Override
	public int insert(Friend friend) {
		try {
//			int no = friendMapper.plusSeq();
//			System.out.println("no" + no);
			System.out.println("friend : " + friend.getFriendId());
			int result = friendMapper.insert(friend);
			
			return result;
		} catch (Exception e) {
			return 0;
		}
		
	}
	
	@Override
	public int select(Friend friend) {
		try {
			if (friendMapper.select(friend) == null) {
				return 0;
			} else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	//친구 검색
	@Override
	public Member search(Member member) {
		try {
			System.out.println("search 들어옴=============================================== " );
			System.out.println("넘어온 Member : " + member);
			Member row = memberMapper.select(member);
			System.out.println("selectByName 결과 : " + row);
			
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
	
	@Override
	public List<Member> selectByName(Member member) {
		try {
			System.out.println("FriendServiceImpl 클래스 selectByName 메소드 들어옴");
			System.out.println("member ID : " + member.getId() + "|| member Name : " + member.getName());
			List<Member> rows = friendMapper.selectByName(member);
			
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
