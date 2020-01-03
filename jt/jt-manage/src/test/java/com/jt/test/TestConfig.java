package com.jt.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/properties/redis.properties")
public class TestConfig {
	
	@Value("${redis.host}")
	private String host;
	@Value("${redis.port}")
	private Integer port;
	
	
	@Test
	public void sss() {
		System.out.println(host+port);
	}
}
