package com.jt;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.thread.ThreadPool;

import redis.clients.jedis.JedisCluster;

//模拟窗口卖票业务
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedislLockTest{
	private int num = 5000; //模拟5000张火车票
	@Autowired
	@Qualifier("redisLock")
	private Lock lock;
	@Autowired
	private JedisCluster jedisCluster;

	class window implements Runnable {
		//定义线程卖票操作
		@Override
		public void run() {
			while(true) {
				lock.lock(); //加锁
					if(num>0) {
						System.out.println(Thread.currentThread().getName()+"售出第"+(5001-num)+"张票");
						String ticket = jedisCluster.hget("windows",Thread.currentThread().getName());
						jedisCluster.hset("windows",Thread.currentThread().getName(), ticket+","+(5001-num));
						num--;
					}else {
						break;//跳出循环
					}
				lock.unlock(); //解锁
			}
		}
	}


	@Test
	public void test01() {
		Runnable window = new window();
		//定义线程池 初始化10个线程
		List<Thread> pools = ThreadPool.initPool(window,10);
		for (Thread thread : pools) {
			thread.start();
		}
		for(;;); //防止线程提前结束
	}
}
