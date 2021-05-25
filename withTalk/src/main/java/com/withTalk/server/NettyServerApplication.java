package com.withTalk.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.withTalk.server.nettyserver.NettyServer;

@SpringBootApplication
@ComponentScan(basePackages = {"com.withTalk.server"})
@MapperScan(basePackages = {"com.withTalk.server"})
public class NettyServerApplication implements CommandLineRunner {
	@Autowired
	NettyServer nettyServer;
	
	public static void main(String[] args) {
		SpringApplication.run(NettyServerApplication.class,args);
	}

	public void run(String... args) throws Exception {
		//원격 저장소
		nettyServer.start();
	}
}
