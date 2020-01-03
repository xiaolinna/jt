package com.jt.lock;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jt.mapper.MysqlMapper;
import com.jt.stream.LuaStream;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.SetParams;
@Component("redisLock")
public class RedisLock implements Lock{
	
	@Autowired
	private JedisCluster jedisCluster;
	private ThreadLocal<String> thread = new ThreadLocal<>();
	private static final String key = "JT_LOCK";
	
	//表示尝试加锁
	@Override
	public boolean tryLock() {
		try {
			String value = UUID.randomUUID().toString();
			SetParams params = new SetParams();
			params.nx().ex(800);
			String result = jedisCluster.set(key, value);
			if(result.equalsIgnoreCase("ok")) {
				thread.set(value);
				return true;	//加锁成功!!!
			}
		} catch (Exception e) {
			return false;		//加锁失败
		}	
		return false;			//加锁失败
	}

	//表示加锁机制
	@Override
	public void lock() {
		if(tryLock()) {
			//表示加锁成功
			return;
		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lock();
	}

	@Override
	public void unlock() {
		String	uuid = thread.get();
		//所以使用lua脚本实现.保证数据的原子性操作
		String script = LuaStream.getLuaFileString();
		jedisCluster.eval(script, Arrays.asList(key), Arrays.asList(uuid));
	}
	
	
	
	
	
	
	
	
	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}
}
