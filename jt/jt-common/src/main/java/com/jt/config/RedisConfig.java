package com.jt.config;

import java.util.HashSet;
import java.util.Set;

import org.apache.tomcat.jni.Pool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;

@Configuration	//标识配置类
//引入主启动类所在项目的配置文件.
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {

	
	//集群配置
	@Value("${cluster.nodes}")
	private String nodes;
	
	@Scope("prototype")
	@Bean
	public JedisCluster jedisCluster() {
		
		String[] split = nodes.split(",");
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		for (String node : split) {
			String host = node.split(":")[0];
			Integer port = Integer.parseInt(node.split(":")[1]);
			nodes.add(new HostAndPort(host, port));
		}
		return new JedisCluster(nodes); 
	}
	
	
	
	//实现哨兵
//	@Value("${redis.nodes}")
//	private String nodes;
//	
//	@Bean
//	public JedisSentinelPool pool() {
//		
//		Set<String> sentinels = new HashSet<>();	
//		sentinels.add(nodes);
//		JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels);
//		
//		return pool;
//	}
//	
//	@Bean
//	@Scope("prototype")
//	public Jedis jedis(JedisSentinelPool pool) {
//
//		return pool.getResource();
//	}
	

//	//实现多台Redis配置
//	@Value("${redis.nodes}")
//	private String nodes;
//	
//	//多个用户,要用多例
//	@Scope("prototype")
//	@Bean
//	public ShardedJedis shardedJedis() {
//		
//		List<JedisShardInfo> shards = new ArrayList<>();
//		String[] arrayNode = nodes.split(",");
//		for (String node : arrayNode) {
//			String host = node.split(":")[0];
//			int port = Integer.parseInt(node.split(":")[1]);
//			shards.add(new JedisShardInfo(host, port));
//		}
//		return new ShardedJedis(shards);
//	}
	
	
	//实现单台redis配置
//	@Value("${redis.host}")
//	private String host;
//	@Value("${redis.port}")
//	private Integer port;
	
//	@Scope("prototype")
//	@Bean
//	public Jedis jedis() {
//		
//		return new Jedis(host, port);
//	}
	

	
}
