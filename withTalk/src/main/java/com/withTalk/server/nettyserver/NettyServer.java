package com.withTalk.server.nettyserver;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.withTalk.server.model.ChatRoom;
import com.withTalk.server.repository.ChatRoomMapper;
import com.withTalk.server.repository.JoinChatRoomMapper;
import com.withTalk.server.repository.MemberMapper;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

@Component
public class NettyServer {
	@Autowired
	private ServerBootstrap sb;
	@Autowired
	private InetSocketAddress port;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private ChatRoomMapper chatRoomMapper;
	@Autowired
	private JoinChatRoomMapper joinChatRoomMapper;
	@Autowired
	private Map<String, Channel> mappingMember;
	@Autowired
<<<<<<< HEAD
	public Map<Integer, Set<String>> chatRoomMap;
	
	public static final String SUCCESS = "r200";
	public static final String FAIL = "r400";
=======
	private Map<Integer, Set<String>> chatRoomMap;
>>>>>>> branch 'main' of https://github.com/gnwcho/WithTalk.git

	private ChannelFuture cf;

	public void start() throws Exception {
		this.initMappingMember();
		this.initMappingChatRoom();
		
		cf = sb.bind(port).sync();
		cf.channel().closeFuture().sync();
	}

	public void initMappingMember() {
		List<String> listId = new ArrayList<>();

		try {
			listId = memberMapper.selectAllId();
			Iterator<String> it = listId.iterator();

			while (it.hasNext()) {
				mappingMember.put((String) it.next(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initMappingChatRoom() {
		List<ChatRoom> chatRoomList = null;
		List<String> joinChatRoomList = null;
		Set<String> setId = null;

		try {
			// 대화방 검색
			chatRoomList = chatRoomMapper.selectAll();

			for (ChatRoom room : chatRoomList) {
				// 방 id에 속한 회원 찾기
				joinChatRoomList = joinChatRoomMapper.selectMemberByRoomId(room);
				
				// 방에 속한 회원 출력 test
				/*for(int i = 0; i < joinChatRoomList.size(); i++) {
					System.out.println(room.getSequenceNo() + ": " + joinChatRoomList.get(i));
				}*/
				
				setId = new HashSet<>();
				for (String participationId : joinChatRoomList) {
					setId.add(participationId);
				}
				
				chatRoomMap.put(room.getSequenceNo(), setId);
			}
			
			Set<Integer> test = chatRoomMap.keySet();
			Iterator<Integer> it= test.iterator();
			while (it.hasNext()) {
				int roomNo = it.next();
				System.out.println(roomNo + ":" + chatRoomMap.get(roomNo));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
