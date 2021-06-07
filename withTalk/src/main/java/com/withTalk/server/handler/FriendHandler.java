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
import com.withTalk.server.nettyserver.NettyServer;
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

			String method = (String) jsonObj.get("method");
			String result = null;

			switch (method) {
			// 친구 등록
			case "insertFriend":
				friend.setMemberId((String) jsonObj.get("memberId"));
				friend.setFriendId((String) jsonObj.get("friendId"));

				if (friend.getMemberId() != null && friend.getFriendId() != null) {
					if (friend.getMemberId().equals(friend.getFriendId())) {

					} else {
						if (friendServiceImpl.select(friend) == 0) {
							int insertResult = friendServiceImpl.insert(friend);

							resultJson.put("type", type);
							resultJson.put("method", method);
							if (insertResult == 0) {
								resultJson.put("status", NettyServer.FAIL);
							} else {
								resultJson.put("status", NettyServer.SUCCESS);
							}

							ctx.writeAndFlush(resultJson.toJSONString());
							break;
						}
					}
				}
				resultJson.put("type", type);
				resultJson.put("method", method);
				resultJson.put("status", NettyServer.SUCCESS);

				ctx.writeAndFlush(resultJson.toJSONString());
				break;

			// 친구 검색
			case "searchFriend":
				member.setPhoneNo((String) jsonObj.get("phoneNo"));

				resultMember = friendServiceImpl.search(member);

				resultJson.put("type", type);
				resultJson.put("method", method);

				if (resultMember == null) {
					resultJson.put("status", NettyServer.FAIL);
					resultJson.put("id", null);
					resultJson.put("name", null);
					resultJson.put("phoneNo", null);
				} else {
					resultJson.put("status", NettyServer.SUCCESS);
					resultJson.put("id", resultMember.getId());
					resultJson.put("name", resultMember.getName());
					resultJson.put("phoneNo", resultMember.getPhoneNo());
				}

				ctx.writeAndFlush(resultJson.toJSONString());
				break;

			// 친구 목록 조회
			case "selectAllFriend":
				friend.setMemberId((String) jsonObj.get("id"));

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
				resultJson.put("type", type);
				resultJson.put("method", method);

				if (friendJsonList != null) {
					resultJson.put("status", NettyServer.SUCCESS);
					resultJson.put("friendList", friendJsonList);
				} else {
					resultJson.put("status", NettyServer.SUCCESS);
					resultJson.put("friendList", null);
				}
				ctx.writeAndFlush(resultJson.toJSONString());
				break;

			// 등록된 친구 검색
			case "searchRegistFriend":
				member.setName((String) jsonObj.get("searchName"));
				member.setId((String) jsonObj.get("senderId"));

				List<Member> resultMemberList = friendServiceImpl.selectByName(member);

				resultJson.put("type", type);
				resultJson.put("method", "searchRegistFriend");

				if (resultMemberList != null) {
					JSONArray registFriendList = new JSONArray();
					JSONObject registFriend = null;

					for (int i = 0; i < resultMemberList.size(); i++) {
						registFriend = new JSONObject();

						registFriend.put("id", resultMemberList.get(i).getId());
						registFriend.put("name", resultMemberList.get(i).getName());

						registFriendList.add(registFriend);
					}

					resultJson.put("status", NettyServer.SUCCESS);
					resultJson.put("registFriendList", registFriendList);
				} else {
					resultJson.put("status", NettyServer.FAIL);
					resultJson.put("registFriendList", null);
				}

				ctx.writeAndFlush(resultJson.toJSONString());
				break;

			// 친구 삭제
			case "delete":
				friend.setMemberId((String) jsonObj.get("memberId"));
				friend.setFriendId((String) jsonObj.get("friendId"));

				int deleteResult = friendServiceImpl.delete(friend);

				resultJson.put("type", type);
				resultJson.put("method", method);

				if (deleteResult == 0) {
					resultJson.put("status", NettyServer.FAIL);
				} else {
					resultJson.put("status", NettyServer.SUCCESS);
				}

				ctx.writeAndFlush(resultJson.toJSONString());
				break;
			default:
				System.out.println("not found method...");
			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}

}
