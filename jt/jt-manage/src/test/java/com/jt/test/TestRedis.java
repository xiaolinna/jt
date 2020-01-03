package com.jt.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.SetParams;

//@SpringBootTest
//@RunWith(SpringRunner.class)
public class TestRedis {

	private Jedis jedis;
	
	@Autowired
	private ShardedJedis shardsConfid;
	
	@Before
	public void init() {
		jedis = new Jedis("192.168.141.128", 6379);
	}
	
//	@Test
//	public void aaa() {
//		String string  = jedis.jedis().toString();
//		System.out.println(string);
//	}
	
	
	@Test
	public void testString() throws InterruptedException {
		jedis.set("dalao", "啊是大佬,awsl");
		System.out.println(jedis.get("dalao"));
		
		Long flag = jedis.setnx("dalao", "awsl");
		System.out.println("标识符: "+flag);
		System.out.println(jedis.get("dalao"));
		
		jedis.expire("dalao", 10);
		Thread.sleep(3000);
		Long ttl = jedis.ttl("dalao");
		jedis.setex("dalao", 11, "aaaaaa");
		System.out.println(ttl);
		
	}
	
	@Test
	public void testNXEX() {
		
		
		SetParams params = new SetParams();
		params.nx();
		params.ex(20);
		String set = jedis.set("aaa", "aaaa", params);
		System.out.println(set);
		set = jedis.set("aaa", "aaaa", params);
		System.out.println(set);
		
	}
	
	@Test
	public void testHash() {
		
		jedis.hset("person", "id","1");
		jedis.hset("person", "name","dalao");
		Map<String, String> hgetAll = jedis.hgetAll("person");
		System.out.println(hgetAll);
	}
	
	@Test
	public void testList() {

	Long lpush = jedis.lpush("list", "1","2","3","4","5");
	System.out.println(jedis.rpop("list"));
	List<String> lrange = jedis.lrange("list", 0, -1);
	System.out.println(lpush);
	System.out.println(lrange);
	}
	
	@Test
	public void tsetTx() {
		
		Transaction multi = jedis.multi();
		try {
			multi.set("aaaaa", "aaaaa");
			multi.set("aaaaaaaaaa", "aaa");
			multi.exec();
		} catch (Exception e) {
			e.printStackTrace();
			multi.discard();
		}

	}
	
	@Test
	public void TestShards() {
		
		List<JedisShardInfo> shards = new ArrayList<>();
		shards.add(new JedisShardInfo("192.168.141.128", 6379));
		shards.add(new JedisShardInfo("192.168.141.128", 6380));
		shards.add(new JedisShardInfo("192.168.141.128", 6381));
		
		ShardedJedis jedis = new ShardedJedis(shards);
		jedis.set("dalao", "awsl");
		System.out.println(jedis.get("dalao"));
	}
	
	
	
	
	
	
	@Test
	public void testShards() {
		List<JedisShardInfo> shards = new ArrayList<>();
		shards.add(new JedisShardInfo("192.168.141.130", 6382));
		shards.add(new JedisShardInfo("192.168.141.130", 6383));
		shards.add(new JedisShardInfo("192.168.141.130", 6384));

		ShardedJedis jedis = new ShardedJedis(shards);
		jedis.set("aaaaa", "aaaaaaa");
		System.out.println(jedis.get("aaaaa"));
	}
	
	@Test
	public void test1() {
		shardsConfid.set("dalao", "awsla");
		System.out.println(shardsConfid.get("dalao"));
	}
	
	
	@Test
	public void testSentinel() {
		
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.141.128:26379");
		JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels);
		Jedis jedis = pool.getResource();
		jedis.set("dalao", "hello");
		System.out.println(jedis.get("dalao"));
	}
	
	@Test
	public void testCluster() {
		
		HashSet<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.141.128", 7000));
		nodes.add(new HostAndPort("192.168.141.128", 7001));
		nodes.add(new HostAndPort("192.168.141.128", 7002));
		nodes.add(new HostAndPort("192.168.141.128", 7003));
		nodes.add(new HostAndPort("192.168.141.128", 7004));
		nodes.add(new HostAndPort("192.168.141.128", 7005));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("dalao", "hello");
		System.out.println(jedisCluster.get("dalao"));
		
	}
	
	
	

}
