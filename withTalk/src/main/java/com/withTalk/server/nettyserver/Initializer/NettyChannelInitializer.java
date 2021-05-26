package com.withTalk.server.nettyserver.Initializer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.withTalk.server.handler.CommonHandler;
import com.withTalk.server.handler.FriendHandler;
import com.withTalk.server.handler.MemberHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

@Component
public class NettyChannelInitializer extends ChannelInitializer<SocketChannel>{
	private static final StringDecoder STRING_DECODER = new StringDecoder(CharsetUtil.UTF_8);
	private static final StringEncoder STRING_ENCODER = new StringEncoder(CharsetUtil.UTF_8);

	@Autowired
	MemberHandler memberHandler;
	@Autowired
	FriendHandler friendHandler;
	@Autowired
	CommonHandler commonHandler;
	
	@Override
	   protected void initChannel(SocketChannel ch) throws Exception {
	      ChannelPipeline cp = ch.pipeline();
	      cp.addLast(new ByteToMessageDecoder() {
	         @Override
	         public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
	            out.add(in.readBytes(in.readableBytes()));
	         }
	      })
	      .addLast(STRING_DECODER)
	      .addLast(STRING_ENCODER)
	      .addLast(memberHandler)
	      .addLast(commonHandler)
	      .addLast(friendHandler);
	   }
}
