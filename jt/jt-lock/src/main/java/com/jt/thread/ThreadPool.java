package com.jt.thread;

import java.util.ArrayList;
import java.util.List;

public class ThreadPool {
	
	//创建线程池
	public static List<Thread> initPool(Runnable target,int poolSize){
		List<Thread> threads = new ArrayList<Thread>(poolSize);
		for(int i=1;i<=poolSize;i++) {
			Thread thread = new Thread(target, "全国ID:["+i+"]的售票口");
			threads.add(thread);
		}
		return threads;
	}
}
