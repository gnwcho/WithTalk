package com.withTalk.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.withTalk.server.model.Friend;
import com.withTalk.server.model.Member;
import com.withTalk.server.service.FriendServiceImpl;
import com.withTalk.server.service.MemberServiceImpl;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Component
@Sharable
public class FriendHandler extends SimpleChannelInboundHandler<String> {
	@Autowired
	JSONParser parser;
	@Autowired
	public MemberServiceImpl memberServiceImpl;
	@Autowired
	public FriendServiceImpl friendServiceImpl;
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		JSONObject jsonObj = (JSONObject) parser.parse(msg);
		String type = (String) jsonObj.get("type");
		
		if ("friend".equals(type)) {
			Member member = new Member();
			Member resultMember = null;
			
			Friend friend = new Friend();
			Friend resultFriend = null;

			JSONObject resultJson = new JSONObject();

			String result = null;

			String method = (String) jsonObj.get("method");

			switch (method) {
				//친구 등록
				case "insertFriend" :
					friend.setMemberId((String)jsonObj.get("memberId"));
					friend.setFriendId((String)jsonObj.get("friendId"));
					
					int insertResult = friendServiceImpl.insert(friend);
					
					resultJson.put("type", type);
					resultJson.put("method", method);
					if (insertResult == 0) {
						resultJson.put("result", "r400");
					} else {
						resultJson.put("result", "r200");
					}
					
					ctx.writeAndFlush(resultJson.toJSONString());
					break;
	
				//친구 검색
				case "searchFriend" :
					member.setPhoneNo((String)jsonObj.get("phoneNo"));
					
					resultMember = friendServiceImpl.search(member);
					System.out.println("결과 : " + resultMember);
					
					resultJson.put("type", type);
					resultJson.put("method", method);
					
					if (resultMember == null) {
						resultJson.put("status", "r400");
						resultJson.put("id", null);
						resultJson.put("name", null);
						resultJson.put("phoneNo", null);
					} else {
						resultJson.put("status", "r200");
						resultJson.put("id", resultMember.getId());
						resultJson.put("name", resultMember.getName());
						resultJson.put("phoneNo", resultMember.getPhoneNo());
					}
					
					System.out.println("searchFriend 검색 결과 : " + resultJson);
					
					ctx.writeAndFlush(resultJson.toJSONString());
					break;
					
				//친구 목록 조회
				case "selectAllFriend" :
					friend.setMemberId((String)jsonObj.get("id"));
					
					List<Friend> findIdListFriend = new ArrayList<Friend>();
					findIdListFriend = friendServiceImpl.selectAll(friend);
					
					JSONObject friendJson = null;
					
					JSONArray friendJsonList = new JSONArray();
					
					if (findIdListFriend != null) {
						for (int i = 0; i < findIdListFriend.size(); i++) {
							friendJson = new JSONObject();
							member.setId(findIdListFriend.get(i).getFriendId());
							
							resultMember = memberServiceImpl.searchMemberInfo(member);
							
							friendJson.put("name", resultMember.getName());
							friendJson.put("id", member.getId());
							
							friendJsonList.add(friendJson);
						}
					}
					resultJson.put("type", "friend");
					resultJson.put("method", "selectAllFriend");
					
					if (friendJsonList != null) {
						resultJson.put("status", "r200");
						resultJson.put("friendList", friendJsonList);
					} else {
						resultJson.put("status", "r400");
						resultJson.put("friendList", null);
					}
					ctx.writeAndFlush(resultJson.toJSONString());
					break;
					
				//친구 상세 조회
				case "selectFriend" :
					member.setName((String) jsonObj.get("name"));
					List<Member> resultMemberList = new ArrayList<Member>();
					List<Friend> resultFriendList = new ArrayList<Friend>();
					
					resultMemberList = memberServiceImpl.searchMemberInfoList(member);
					
					for (int i = 0; i < resultMemberList.size(); i++) {
						friend.setMemberId(resultMemberList.get(i).);
					}
					
					//TO DO
					
				//친구 삭제
				case "delete" :
					
				default:
					System.out.println("not found method...");
			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	
}
