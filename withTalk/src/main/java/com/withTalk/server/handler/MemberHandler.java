package com.withTalk.server.handler;

import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.withTalk.server.model.Member;
import com.withTalk.server.nettyserver.NettyServer;
import com.withTalk.server.service.MemberServiceImpl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Component
@Sharable
public class MemberHandler extends SimpleChannelInboundHandler<String> {
	@Autowired
	private JSONParser parser;
	@Autowired
	private MemberServiceImpl memberServiceImpl;
	@Autowired
	private Map<String, Channel> mappingMember;

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		try {
			if (msg.length() == 0) {
				System.out.println("잘못된 요청");
			}

			JSONObject jsonObj = (JSONObject) parser.parse(msg);
			String type = (String) jsonObj.get("type");

			if ("member".equals(type)) {
				Member member = new Member();
				Member resultMember = null;

				JSONObject resultJson = new JSONObject();

				String result = null;

				String method = (String) jsonObj.get("method");

				switch (method) {
				// 사용자 인증 (비밀번호 재설정 전)
				case "auth":
					String id = (String) jsonObj.get("id");
					String name = (String) jsonObj.get("name");
					String phoneNo = (String) jsonObj.get("phoneNo");

					resultJson.put("type", type);
					resultJson.put("method", method);

					if (id != null && name != null && phoneNo != null) {
						member.setId(id);
						member.setName(name);
						member.setPhoneNo(phoneNo);

						resultMember = memberServiceImpl.searchMemberInfo(member);
						if (resultMember != null) {
							resultJson.put("status", NettyServer.SUCCESS);
						} else {
							resultJson.put("status", NettyServer.FAIL);
						}

					} else {
						resultJson.put("status", NettyServer.SUCCESS);
					}

					ctx.writeAndFlush(resultJson.toJSONString());
					break;

				// 아이디 찾기
				case "findId":
					member.setName((String) jsonObj.get("name"));
					member.setPhoneNo((String) jsonObj.get("phoneNo"));

					resultMember = memberServiceImpl.searchMemberInfo(member);

					resultJson.put("type", "member");
					resultJson.put("method", method);

					if (resultMember != null) {
						resultJson.put("id", resultMember.getId());
						resultJson.put("status", NettyServer.SUCCESS);
					} else {
						resultJson.put("id", null);
						resultJson.put("status", NettyServer.FAIL);
					}

					ctx.writeAndFlush(resultJson.toJSONString());
					break;

				// 회원가입
				case "signUp":
					id = (String) jsonObj.get("id");
					member.setId((String) jsonObj.get("id"));
					member.setName((String) jsonObj.get("name"));
					member.setPassword((String) jsonObj.get("password"));
					member.setPhoneNo((String) jsonObj.get("phoneNo"));

					result = memberServiceImpl.signUp(member);

					if ((NettyServer.SUCCESS).equals(result)) {
						mappingMember.put(id, null);
					}

					resultJson.put("method", method);
					resultJson.put("status", result);

					ctx.writeAndFlush(resultJson.toJSONString());
					break;

				// 아이디 확인
				case "checkId":
					member.setId((String) jsonObj.get("id"));

					result = memberServiceImpl.checkId(member);

					resultJson.put("method", method);
					resultJson.put("status", result);

					ctx.writeAndFlush(resultJson.toJSONString());
					break;

				// 비밀번호 재설정
				case "resetPassword":
					member.setId((String) jsonObj.get("id"));
					member.setPassword((String) jsonObj.get("newPassword"));

					resultJson.put("method", method);
					resultJson.put("status", result);
					
					if (jsonObj.get("newPassword") != null) {
						int resetResult = memberServiceImpl.updateMemberInfo(member);

						if (resetResult == 0) {
							resultJson.put("status", NettyServer.FAIL);
						} else {
							resultJson.put("status", NettyServer.SUCCESS);
						}
					} else {
						resultJson.put("status", NettyServer.FAIL);
					}

					ctx.writeAndFlush(resultJson.toJSONString());
					break;

				default:
					System.out.println("not found method...");
				}
			} else {
				ctx.fireChannelRead(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
